package ru.poomsae.core.driver.http.dto.requests.discipline

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на обновление дисциплины")
data class UpdateDisciplineRequest(
    @Schema(description = "ID дисциплины", example = "1")
    val id: Long,
    @Schema(description = "Название дисциплины", example = "Туль")
    val name: String,
    @Schema(description = "Флаг удаления", example = "false")
    val deleted: Boolean
)
