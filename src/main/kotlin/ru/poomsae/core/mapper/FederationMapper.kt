package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import ru.poomsae.core.domain.model.Federation
import ru.poomsae.core.driver.http.dto.federation.CreateFederationRequest
import ru.poomsae.core.driver.http.dto.federation.FederationResponse
import ru.poomsae.core.driver.http.dto.federation.UpdateFederationRequest

@Mapper(componentModel = "spring")
interface FederationMapper {
    fun toResponse(federation: Federation): FederationResponse
    fun fromCreateRequest(request: CreateFederationRequest): Federation
    fun fromUpdateRequest(request: UpdateFederationRequest): Federation
}