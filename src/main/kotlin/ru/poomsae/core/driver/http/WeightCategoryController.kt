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
import ru.poomsae.core.domain.WeightCategory
import ru.poomsae.core.driver.http.dto.requests.weightcategory.CreateWeightCategoryRequest
import ru.poomsae.core.driver.http.dto.requests.weightcategory.UpdateWeightCategoryRequest
import ru.poomsae.core.driver.http.dto.responses.WeightCategoryResponse
import ru.poomsae.core.mapper.WeightCategoryMapper
import ru.poomsae.core.service.interfaces.WeightCategoryService

@RestController
@RequestMapping("/weight-categories")
@Tag(name = "WeightCategory", description = "API для управления весовыми категориями")
class WeightCategoryController (
    private val weightCategoryService: WeightCategoryService,
    private val weightCategoryMapper: WeightCategoryMapper
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{id}")
    @Operation(summary = "Получить весовую категорию по ID")
    @ApiResponse(responseCode = "200", description = "Весовая категория найдена")
    @ApiResponse(responseCode = "404", description = "Весовая категория не найдена")
    fun get(
        @Parameter(description = "ID весовой категории")
        @PathVariable id: Long
    ): ResponseEntity<WeightCategoryResponse> {
        val weightCategory = weightCategoryService.get(id)

        requireNotNull(weightCategory) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok(weightCategoryMapper.toResponse(weightCategory))
    }

    @GetMapping
    @Operation(summary = "Получить все весовые категории")
    @ApiResponse(responseCode = "200", description = "Список весовых категорий")
    fun getMany(): ResponseEntity<List<WeightCategoryResponse>> {
        val weightCategories: List<WeightCategory> = weightCategoryService.getMany()
        val response: List<WeightCategoryResponse> = weightCategories.map { weightCategoryMapper.toResponse(it) }
        return ResponseEntity.ok(response)
    }

    @PostMapping
    @Operation(summary = "Создать весовую категорию")
    @ApiResponse(responseCode = "200", description = "Весовая категория создана")
    fun create(
        @RequestBody request: CreateWeightCategoryRequest
    ): ResponseEntity<WeightCategoryResponse> {
        val weightCategory : WeightCategory = weightCategoryService.create(weightCategoryMapper.toEntity(request))
        return ResponseEntity.ok(weightCategoryMapper.toResponse(weightCategory))
    }

    @PutMapping
    @Operation(summary = "Обновить весовую категорию")
    @ApiResponse(responseCode = "200", description = "Весовая категория обновлена")
    fun update(
        @RequestBody request: UpdateWeightCategoryRequest
    ): ResponseEntity<WeightCategoryResponse> {
        val weightCategory : WeightCategory = weightCategoryService.update(weightCategoryMapper.toEntity(request))
        return ResponseEntity.ok(weightCategoryMapper.toResponse(weightCategory))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить весовую категорию")
    @ApiResponse(responseCode = "204", description = "Весовая категория удалена")
    fun delete(
        @Parameter(description = "ID весовой категории")
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        weightCategoryService.delete(id)
        return ResponseEntity.noContent().build()
    }

}