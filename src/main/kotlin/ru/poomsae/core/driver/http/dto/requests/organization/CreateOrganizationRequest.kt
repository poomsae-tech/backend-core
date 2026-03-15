package ru.poomsae.core.driver.http.dto.requests.organization

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Запрос на создание клуба")
data class CreateOrganizationRequest(
    @Schema(description = "Название клуба", example = "Спортивный клуб тхэквондо 'ITF Master'")
    val name: String,
    @Schema(description = "ID региона", example = "1")
    val regionId: Long,
    @Schema(description = "ИНН клуба", example = "7701123456")
    val inn: String,
    @Schema(description = "Адрес клуба", example = "г. Москва, ул. Спортивная, д. 10, стр. 2")
    val address: String
)