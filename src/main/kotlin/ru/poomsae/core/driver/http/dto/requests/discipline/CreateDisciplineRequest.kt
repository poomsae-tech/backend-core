package ru.poomsae.core.driver.http.dto.requests.discipline

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на создание дисциплины")
data class CreateDisciplineRequest (
    @Schema(description = "Название дисциплины", example = "Туль")
    val name: String
)