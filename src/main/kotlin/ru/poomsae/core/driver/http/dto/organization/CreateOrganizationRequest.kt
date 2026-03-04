package ru.poomsae.core.driver.http.dto.organization

import ru.poomsae.core.domain.model.Organization
import ru.poomsae.core.domain.model.OrganizationStatus

data class CreateOrganizationRequest(
    val name: String,
    val regionId: Long,
    val inn: String,
    val address: String,
    val status: OrganizationStatus,
    val createdBy: String
) {
    fun toDomain() = Organization(
        name = name,
        regionId = regionId,
        inn = inn,
        address = address,
        status = status,
        createdBy = createdBy
    )
}