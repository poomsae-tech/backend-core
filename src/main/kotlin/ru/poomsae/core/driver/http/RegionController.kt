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
import ru.poomsae.core.domain.Region
import ru.poomsae.core.driver.http.dto.requests.region.CreateRegionRequest
import ru.poomsae.core.driver.http.dto.requests.region.UpdateRegionRequest
import ru.poomsae.core.driver.http.dto.responses.RegionResponse
import ru.poomsae.core.mapper.RegionMapper
import ru.poomsae.core.service.interfaces.RegionService

@RestController
@RequestMapping("/regions")
@Tag(name = "Region", description = "API для управления регионами")
class RegionController(
    private val regionService: RegionService,
    private val regionMapper: RegionMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{id}")
    @Operation(summary = "Получить регион по ID")
    @ApiResponse(responseCode = "200", description = "Регион найдена")
    @ApiResponse(responseCode = "404", description = "Регион не найдена")
    fun get(
        @Parameter(description = "ID федерации")
        @PathVariable id: Long
    ): ResponseEntity<RegionResponse> {
        val region = regionService.get(id)

        requireNotNull(region) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok(regionMapper.toResponse(region))
    }

    @GetMapping
    @Operation(summary = "Получить все регионы")
    @ApiResponse(responseCode = "200", description = "Список регионов")
    fun getMany(): ResponseEntity<List<RegionResponse>> {
        val federations: List<Region> = regionService.getMany()
        val response: List<RegionResponse> = federations.map { regionMapper.toResponse(it) }
        return ResponseEntity.ok(response)
    }

    @PostMapping
    @Operation(summary = "Создать регион")
    @ApiResponse(responseCode = "200", description = "Регион создан")
    fun create(
        @RequestBody request: CreateRegionRequest
    ): ResponseEntity<RegionResponse> {
        val federation: Region = regionService.create(regionMapper.toEntity(request))
        return ResponseEntity.ok(regionMapper.toResponse(federation))
    }

    @PutMapping
    @Operation(summary = "Обновить регион")
    @ApiResponse(responseCode = "200", description = "Регион обновлен")
    fun update(
        @RequestBody request: UpdateRegionRequest
    ): ResponseEntity<RegionResponse> {
        val federation: Region = regionService.update(regionMapper.toEntity(request))
        return ResponseEntity.ok(regionMapper.toResponse(federation))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить регион")
    @ApiResponse(responseCode = "204", description = "Регион удален")
    fun delete(
        @Parameter(description = "ID федерации")
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        regionService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
