package ru.poomsae.core.driver.http.dto.federation

import ru.poomsae.core.domain.model.FederationType

data class FederationResponse(
    val id: Long,
    val name: String,
    val regionId: Long?,
    val type: FederationType
)