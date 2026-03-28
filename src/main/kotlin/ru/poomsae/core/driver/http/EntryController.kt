package ru.poomsae.core.driver.http

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.poomsae.core.domain.AuthenticatedUser
import ru.poomsae.core.domain.EntryStatus
import ru.poomsae.core.driver.http.dto.requests.entry.CreateEntryRequest
import ru.poomsae.core.driver.http.dto.requests.entry.UpdateEntryStatusRequest
import ru.poomsae.core.driver.http.dto.responses.entry.EntryResponse
import ru.poomsae.core.mapper.EntryMapper
import ru.poomsae.core.service.interfaces.EntryService

@RestController
@RequestMapping("/api/v1/entries")
@Tag(name = "Entry", description = "API для подачи и обработки заявок на турнир")
class EntryController(
    private val entryService: EntryService,
    private val entryMapper: EntryMapper
) {
    
    @PostMapping
    @Operation(summary = "Подать заявку на турнир")
    @ApiResponse(responseCode = "200", description = "Заявка создана")
    @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    @ApiResponse(responseCode = "403", description = "Недостаточно прав")
    fun createEntry(
        @RequestBody request: CreateEntryRequest,
        @AuthenticationPrincipal user: AuthenticatedUser
    ): ResponseEntity<EntryResponse> {
        // Проверка роли (тренер)
        require(user.hasRole("coach")) {
            throw SecurityException("Только тренер может подавать заявки")
        }
        
        val entry = entryService.createEntry(
            request = request,
            coachId = user.userId.toLongOrNull() ?: throw SecurityException("Неверный ID пользователя"),
            organizationId = user.orgId?.toLongOrNull() ?: throw SecurityException("Организация не указана")
        )
        
        // Пока возвращаем базовый ответ (без спортсменов)
        // TODO: добавить список спортсменов, когда сервис будет возвращать их
        return ResponseEntity.ok(
            EntryResponse(
                id = entry.id!!,
                tournamentId = entry.tournamentId,
                coachId = entry.coachId,
                organizationId = entry.organizationId,
                status = entry.status,
                rejectionReason = entry.rejectionReason,
                athletes = emptyList(), // временно пусто
                createdAt = entry.createdAt
            )
        )
    }
    
    @GetMapping("/tournament/{tournamentId}")
    @Operation(summary = "Получить все заявки на турнир")
    @ApiResponse(responseCode = "200", description = "Список заявок")
    fun getEntriesByTournament(
        @PathVariable tournamentId: Long,
        @RequestParam(required = false) status: EntryStatus?,
        @AuthenticationPrincipal user: AuthenticatedUser
    ): ResponseEntity<List<EntryResponse>> {
        // Проверка роли (судья, админ, президент)
        require(user.hasRole("judge") || user.isSuperAdmin()) {
            throw SecurityException("Недостаточно прав для просмотра заявок")
        }
        
        val entries = entryService.getEntriesByTournament(tournamentId, status)
        
        // Временно возвращаем упрощённые ответы
        val responses = entries.map { entry ->
            EntryResponse(
                id = entry.id!!,
                tournamentId = entry.tournamentId,
                coachId = entry.coachId,
                organizationId = entry.organizationId,
                status = entry.status,
                rejectionReason = entry.rejectionReason,
                athletes = emptyList(), // временно пусто
                createdAt = entry.createdAt
            )
        }
        
        return ResponseEntity.ok(responses)
    }
    
    @PatchMapping("/{entryId}/status")
    @Operation(summary = "Изменить статус заявки")
    @ApiResponse(responseCode = "200", description = "Статус обновлён")
    fun updateEntryStatus(
        @PathVariable entryId: Long,
        @RequestBody request: UpdateEntryStatusRequest,
        @AuthenticationPrincipal user: AuthenticatedUser
    ): ResponseEntity<EntryResponse> {
        require(user.hasRole("judge") || user.isSuperAdmin()) {
            throw SecurityException("Недостаточно прав для изменения статуса")
        }
        
        val entry = entryService.updateEntryStatus(
            entryId = entryId,
            newStatus = request.status,
            rejectionReason = request.rejectionReason
        )
        
        return ResponseEntity.ok(
            EntryResponse(
                id = entry.id!!,
                tournamentId = entry.tournamentId,
                coachId = entry.coachId,
                organizationId = entry.organizationId,
                status = entry.status,
                rejectionReason = entry.rejectionReason,
                athletes = emptyList(), // временно пусто
                createdAt = entry.createdAt
            )
        )
    }
}