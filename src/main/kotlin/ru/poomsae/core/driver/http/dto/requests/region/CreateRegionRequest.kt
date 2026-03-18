package ru.poomsae.core.driver.http.dto.requests.region

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на создание региона")
data class CreateRegionRequest(
    @Schema(description = "Название региона", example = "Москва")
    val name: String
)

