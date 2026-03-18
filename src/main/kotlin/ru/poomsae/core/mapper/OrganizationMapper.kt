package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import ru.poomsae.core.domain.Organization
import ru.poomsae.core.domain.OrganizationStatus
import ru.poomsae.core.driver.http.dto.requests.organization.CreateOrganizationRequest
import ru.poomsae.core.driver.http.dto.responses.OrganizationResponse
import ru.poomsae.core.driver.http.dto.requests.organization.UpdateOrganizationRequest

@Mapper(componentModel = "spring")
interface OrganizationMapper {
    @Mapping(target = "status", qualifiedByName = ["statusToString"])
    fun toResponse(organization: Organization): OrganizationResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    fun toEntity(request: CreateOrganizationRequest): Organization

    @Mapping(target = "status", qualifiedByName = ["stringToStatus"])
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: UpdateOrganizationRequest): Organization

    @Named("statusToString")
    fun statusToString(status: OrganizationStatus): String = status.name

    @Named("stringToStatus")
    fun stringToStatus(status: String?): OrganizationStatus? = status?.let { OrganizationStatus.valueOf(it) }
}