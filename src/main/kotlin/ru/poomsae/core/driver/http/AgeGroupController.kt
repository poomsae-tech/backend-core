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
import ru.poomsae.core.domain.AgeGroup
import ru.poomsae.core.driver.http.dto.requests.agegroup.CreateAgeGroupRequest
import ru.poomsae.core.driver.http.dto.requests.agegroup.UpdateAgeGroupRequest
import ru.poomsae.core.driver.http.dto.responses.AgeGroupResponse
import ru.poomsae.core.mapper.AgeGroupMapper
import ru.poomsae.core.service.interfaces.AgeGroupService

@RestController
@RequestMapping("/age-groups")
@Tag(name = "AgeGroup", description = "API для управления возрастными группами")
class AgeGroupController (
    private val ageGroupService: AgeGroupService,
    private val ageGroupMapper: AgeGroupMapper
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{id}")
    @Operation(summary = "Получить возрастную группу по ID")
    @ApiResponse(responseCode = "200", description = "Возрастная группа найдена")
    @ApiResponse(responseCode = "404", description = "Возрастная группа не найдена")
    fun get(
        @Parameter(description = "ID возрастной группы")
        @PathVariable id: Long
    ): ResponseEntity<AgeGroupResponse> {
        val ageGroup = ageGroupService.get(id)

        requireNotNull(ageGroup) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok(ageGroupMapper.toResponse(ageGroup))
    }

    @GetMapping
    @Operation(summary = "Получить все возрастные группы")
    @ApiResponse(responseCode = "200", description = "Список возрастных групп")
    fun getMany(): ResponseEntity<List<AgeGroupResponse>> {
        val ageGroups: List<AgeGroup> = ageGroupService.getMany()
        val response: List<AgeGroupResponse> = ageGroups.map { ageGroupMapper.toResponse(it) }
        return ResponseEntity.ok(response)
    }

    @PostMapping
    @Operation(summary = "Создать возрастную группу")
    @ApiResponse(responseCode = "200", description = "Возрастная группа создана")
    fun create(
        @RequestBody request: CreateAgeGroupRequest
    ): ResponseEntity<AgeGroupResponse> {
        val ageGroup: AgeGroup = ageGroupService.create(ageGroupMapper.toEntity(request))
        return ResponseEntity.ok(ageGroupMapper.toResponse(ageGroup))
    }

    @PutMapping
    @Operation(summary = "Обновить возрастную группу")
    @ApiResponse(responseCode = "200", description = "Возрастная группа обновлена")
    fun update(
        @RequestBody request: UpdateAgeGroupRequest
    ): ResponseEntity<AgeGroupResponse> {
        val ageGroup: AgeGroup = ageGroupService.update(ageGroupMapper.toEntity(request))
        return ResponseEntity.ok(ageGroupMapper.toResponse(ageGroup))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить возрастную группу")
    @ApiResponse(responseCode = "204", description = "Возрастная группа удалена")
    fun delete(
        @Parameter(description = "ID возрастной группы")
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        ageGroupService.delete(id)
        return ResponseEntity.noContent().build()
    }

}