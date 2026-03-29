package ru.poomsae.core.driver.http

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.poomsae.core.domain.BeltLevel
import ru.poomsae.core.driver.http.dto.requests.beltlevel.CreateBeltLevelRequest
import ru.poomsae.core.driver.http.dto.requests.beltlevel.UpdateBeltLevelRequest
import ru.poomsae.core.driver.http.dto.responses.BeltLevelResponse
import ru.poomsae.core.mapper.BeltLevelMapper
import ru.poomsae.core.service.interfaces.BeltLevelService

@RestController
@RequestMapping("/belt-levels")
@Tag(name = "BeltLevel", description = "API для управления уровнями поясов")
class BeltLevelController (
    private val beltLevelService: BeltLevelService,
    private val beltLevelMapper: BeltLevelMapper
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{id}")
    @Operation(summary = "Получить уровень пояса по ID")
    @ApiResponse(responseCode = "200", description = "Уровень пояса найден")
    @ApiResponse(responseCode = "404", description = "Уровень пояса не найден")
    fun get(
        @Parameter(description = "ID уровня пояса")
        @PathVariable id: Long
    ): ResponseEntity<BeltLevelResponse> {
        val beltLevel = beltLevelService.get(id)

        requireNotNull(beltLevel) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok(beltLevelMapper.toResponse(beltLevel))
    }

    @GetMapping
    @Operation(summary = "Получить все уровни поясов")
    @ApiResponse(responseCode = "200", description = "Список уровней поясов")
    fun getMany(): ResponseEntity<List<BeltLevelResponse>> {
        val beltLevels: List<BeltLevel> = beltLevelService.getMany()
        val response: List<BeltLevelResponse> = beltLevels.map { beltLevelMapper.toResponse(it) }
        return ResponseEntity.ok(response)
    }

    @PostMapping
    @Operation(summary = "Создать уровень пояса")
    @ApiResponse(responseCode = "200", description = "Уровень пояса создана")
    fun create(
        @RequestBody request: CreateBeltLevelRequest
    ): ResponseEntity<BeltLevelResponse> {
        val beltLevel: BeltLevel = beltLevelService.create(beltLevelMapper.toEntity(request))
        return ResponseEntity.ok(beltLevelMapper.toResponse(beltLevel))
    }

    @PutMapping
    @Operation(summary = "Обновить уровень пояса")
    @ApiResponse(responseCode = "200", description = "Уровень пояса обновлен")
    fun update(
        @RequestBody request: UpdateBeltLevelRequest
    ): ResponseEntity<BeltLevelResponse> {
        val beltLevel : BeltLevel = beltLevelService.update(beltLevelMapper.toEntity(request))
        return ResponseEntity.ok(beltLevelMapper.toResponse(beltLevel))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить уровень пояса")
    @ApiResponse(responseCode = "204", description = "Уровень пояса удалён")
    fun delete(
        @Parameter(description = "ID уровня пояса")
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        beltLevelService.delete(id)
        return ResponseEntity.noContent().build()
    }

}