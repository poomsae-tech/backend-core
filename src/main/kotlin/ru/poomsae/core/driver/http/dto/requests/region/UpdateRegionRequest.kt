package ru.poomsae.core.driver.http.dto.requests.region

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на обновление региона")
data class UpdateRegionRequest(
    @Schema(description = "ID региона", example = "1")
    val id: Long,
    @Schema(description = "Название региона", example = "Москва")
    val name: String,
    @Schema(description = "Флаг удаления", example = "false")
    val deleted: Boolean
)
