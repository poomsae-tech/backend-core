package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import ru.poomsae.core.domain.model.Organization
import ru.poomsae.core.driver.http.dto.organization.CreateOrganizationRequest
import ru.poomsae.core.driver.http.dto.organization.OrganizationResponse
import ru.poomsae.core.driver.http.dto.organization.UpdateOrganizationRequest

@Mapper(componentModel = "spring")
interface OrganizationMapper {
    fun toResponse(organization: Organization): OrganizationResponse
    fun fromCreateRequest(request: CreateOrganizationRequest): Organization
    fun fromUpdateRequest(request: UpdateOrganizationRequest): Organization
}