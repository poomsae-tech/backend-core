package ru.poomsae.core.driver.http.dto.responses

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Ответ с данными возрастной группы")
data class AgeGroupResponse(

    @Schema(description = "ID возрастной группы", example = "1")
    val id: Long,

    @Schema(description = "Название возрастной группы", example = "Юниоры")
    val name: String,

    @Schema(description = "Минимальный возраст", example = "14")
    val minAge: Int,

    @Schema(description = "Максимальный возраст", example = "17")
    val maxAge: Int,

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