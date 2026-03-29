package ru.poomsae.core.driver.http.dto.responses

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Ответ с данными весовой категории")
data class WeightCategoryResponse(

    @Schema(description = "ID весовой категории", example = "1")
    val id: Long,

    @Schema(description = "Название категории", example = "До 68 кг")
    val name: String,

    @Schema(description = "Минимальный вес", example = "63.1")
    val minWeight: Float,

    @Schema(description = "Максимальный вес", example = "68.0")
    val maxWeight: Float,

    @Schema(description = "Пол (Мужской / Женский)", example = "Мужской")
    val gender: String,

    @Schema(description = "Флаг удаления", example = "false")
    val deleted: Boolean,

    @Schema(description = "Дата создания", example = "2026-03-15T10:00:00Z")
    val createdAt: Instant,

    @Schema(description = "ID создателя", example = "1")
    val createdBy: Long,

    @Schema(description = "Дата обновления", example = "2026-03-15T12:00:00Z")
    val updatedAt: Instant?,

    @Schema(description = "ID обновившего", example = "1")
    val updatedBy: Long?
)