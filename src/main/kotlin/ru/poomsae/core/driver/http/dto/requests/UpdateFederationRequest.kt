package ru.poomsae.core.driver.http.dto.requests

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на обновление федерации")
data class UpdateFederationRequest(
    @Schema(description = "ID федерации", example = "1")
    val id: Long,
    @Schema(description = "Название федерации", example = "Российская федерация тхэквондо")
    val name: String,
    @Schema(description = "Флаг удаления", example = "false")
    val deleted: Boolean
)
