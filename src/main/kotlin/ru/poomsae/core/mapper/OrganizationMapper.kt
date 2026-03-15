package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import ru.poomsae.core.domain.Organization
import ru.poomsae.core.driver.http.dto.requests.organization.CreateOrganizationRequest
import ru.poomsae.core.driver.http.dto.responses.OrganizationResponse
import ru.poomsae.core.driver.http.dto.requests.organization.UpdateOrganizationRequest

@Mapper(componentModel = "spring")
interface OrganizationMapper {
    fun toResponse(organization: Organization): OrganizationResponse

    fun toEntity(request: CreateOrganizationRequest): Organization

    fun toEntity(request: UpdateOrganizationRequest): Organization
}