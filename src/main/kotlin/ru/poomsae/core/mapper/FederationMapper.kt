package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import ru.poomsae.core.Federation
import ru.poomsae.core.driver.http.dto.requests.CreateFederationRequest
import ru.poomsae.core.driver.http.dto.requests.UpdateFederationRequest
import ru.poomsae.core.driver.http.dto.responses.FederationResponse

@Mapper
interface FederationMapper {

  companion object {
    val INSTANCE: FederationMapper = Mappers.getMapper(FederationMapper::class.java)
  }

  fun toResponse(federation: Federation): FederationResponse

  fun toEntity(createRequest: CreateFederationRequest): Federation

  fun toEntity(updateRequest: UpdateFederationRequest): Federation
}

