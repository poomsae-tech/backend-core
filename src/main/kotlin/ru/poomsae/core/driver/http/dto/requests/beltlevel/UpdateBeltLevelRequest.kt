package ru.poomsae.core.driver.http.dto.requests.beltlevel

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на обновление федерации")
data class UpdateBeltLevelRequest(
    @Schema(description = "ID уровня пояса", example = "1")
    val id: Long,

    @Schema(description = "Название уровня пояса", example = "Чёрный 1 Дан")
    val name: String,

    @Schema(description = "Флаг удаления", example = "false")
    val deleted: Boolean
)
