package ru.poomsae.core.driver.http.dto.organization

import ru.poomsae.core.domain.model.OrganizationStatus

data class OrganizationResponse(
    val id: Long,
    val name: String,
    val inn: String,
    val address: String,
    val status: OrganizationStatus,
    val federationId: Long?,
    val regionId: Long?
)