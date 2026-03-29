package ru.poomsae.core.driver.http.dto.requests.weightcategory

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на создание весовой категории")
data class CreateWeightCategoryRequest (
    @Schema(description = "Название весовой категории", example = "До 68 кг")
    val name: String,

    @Schema(description = "Минимальный вес (кг)", example = "63.1")
    val minWeight: Float,

    @Schema(description = "Максимальный вес (кг)", example = "68.0")
    val maxWeight: Float,

    @Schema(description = "Пол (Мужской / Женский)", example = "Мужской")
    val gender: String
)