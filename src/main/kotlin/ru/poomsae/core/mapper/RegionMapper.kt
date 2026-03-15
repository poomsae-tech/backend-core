package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import ru.poomsae.core.domain.Region
import ru.poomsae.core.driver.http.dto.requests.region.CreateRegionRequest
import ru.poomsae.core.driver.http.dto.responses.RegionResponse
import ru.poomsae.core.driver.http.dto.requests.region.UpdateRegionRequest

@Mapper(componentModel = "spring")
interface RegionMapper {
    fun toResponse(region: Region): RegionResponse

    fun toEntity(request: CreateRegionRequest): Region

    fun toEntity(request: UpdateRegionRequest): Region
}