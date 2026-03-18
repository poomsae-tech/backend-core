package ru.poomsae.core.driver.http.dto.requests.federation

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на создание федерации")
data class CreateFederationRequest (
    @Schema(description = "Название федерации", example = "Российская федерация тхэквондо")
    val name: String
)