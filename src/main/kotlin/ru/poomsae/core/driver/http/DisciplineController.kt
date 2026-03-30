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
import ru.poomsae.core.domain.Discipline
import ru.poomsae.core.driver.http.dto.requests.discipline.CreateDisciplineRequest
import ru.poomsae.core.driver.http.dto.requests.discipline.UpdateDisciplineRequest
import ru.poomsae.core.driver.http.dto.responses.DisciplineResponse
import ru.poomsae.core.mapper.DisciplineMapper
import ru.poomsae.core.service.interfaces.DisciplineService

@RestController
@RequestMapping("/disciplines")
@Tag(name = "Discipline", description = "API для управления дисциплинами")
class DisciplineController (
    private val disciplineService: DisciplineService,
    private val disciplineMapper: DisciplineMapper
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{id}")
    @Operation(summary = "Получить дисциплину по ID")
    @ApiResponse(responseCode = "200", description = "Дисциплина найдена")
    @ApiResponse(responseCode = "404", description = "Дисциплина не найдена")
    fun get(
        @Parameter(description = "ID дисциплины")
        @PathVariable id: Long
    ): ResponseEntity<DisciplineResponse> {
        val discipline = disciplineService.get(id)

        requireNotNull(discipline) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok(disciplineMapper.toResponse(discipline))
    }

    @GetMapping
    @Operation(summary = "Получить все дисциплины")
    @ApiResponse(responseCode = "200", description = "Список дисциплин")
    fun getMany(): ResponseEntity<List<DisciplineResponse>> {
        val disciplines: List<Discipline> = disciplineService.getMany()
        val response: List<DisciplineResponse> = disciplines.map { disciplineMapper.toResponse(it) }
        return ResponseEntity.ok(response)
    }

    @PostMapping
    @Operation(summary = "Создать дисциплину")
    @ApiResponse(responseCode = "200", description = "Дисциплина создана")
    fun create(
        @RequestBody request: CreateDisciplineRequest
    ): ResponseEntity<DisciplineResponse> {
        val discipline : Discipline = disciplineService.create(disciplineMapper.toEntity(request))
        return ResponseEntity.ok(disciplineMapper.toResponse(discipline))
    }

    @PutMapping
    @Operation(summary = "Обновить дисциплину")
    @ApiResponse(responseCode = "200", description = "Дисциплина обновлена")
    fun update(
        @RequestBody request: UpdateDisciplineRequest
    ): ResponseEntity<DisciplineResponse> {
        val discipline : Discipline = disciplineService.update(disciplineMapper.toEntity(request))
        return ResponseEntity.ok(disciplineMapper.toResponse(discipline))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить дисциплину")
    @ApiResponse(responseCode = "204", description = "Дисциплина удалена")
    fun delete(
        @Parameter(description = "ID дисциплины")
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        disciplineService.delete(id)
        return ResponseEntity.noContent().build()
    }

}