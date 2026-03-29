package ru.poomsae.core.driver.http.dto.requests.agegroup

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на обновление федерации")
data class UpdateAgeGroupRequest(
    @Schema(description = "ID возрастной группы", example = "1")
    val id: Long,

    @Schema(description = "Название возрастной группы", example = "Юниоры")
    val name: String,

    @Schema(description = "Минимальный возраст", example = "14")
    val minAge: Int,

    @Schema(description = "Максимальный возраст", example = "17")
    val maxAge: Int,

    @Schema(description = "Флаг удаления", example = "false")
    val deleted: Boolean
)
