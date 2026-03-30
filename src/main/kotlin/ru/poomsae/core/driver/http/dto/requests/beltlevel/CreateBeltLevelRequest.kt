package ru.poomsae.core.driver.http.dto.requests.beltlevel

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на создание федерации")
data class CreateBeltLevelRequest (
    @Schema(description = "Название уровня пояса", example = "Чёрный 1 Дан")
    val name: String
)