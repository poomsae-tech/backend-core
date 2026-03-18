package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.poomsae.core.domain.Federation
import ru.poomsae.core.driver.http.dto.requests.federation.CreateFederationRequest
import ru.poomsae.core.driver.http.dto.requests.federation.UpdateFederationRequest
import ru.poomsae.core.driver.http.dto.responses.FederationResponse

@Mapper(componentModel = "spring")
interface FederationMapper {

    fun toResponse(federation: Federation): FederationResponse

    @Mapping(target = "regionId", expression = "java(1L)")
    @Mapping(target = "federationType", constant = "ALL_RUSSIAN")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    fun toEntity(createRequest: CreateFederationRequest): Federation

    @Mapping(target = "regionId", expression = "java(1L)")
    @Mapping(target = "federationType", constant = "ALL_RUSSIAN")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(updateRequest: UpdateFederationRequest): Federation
}

