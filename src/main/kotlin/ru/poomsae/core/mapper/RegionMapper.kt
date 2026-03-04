package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import ru.poomsae.core.domain.model.Region
import ru.poomsae.core.driver.http.dto.region.CreateRegionRequest
import ru.poomsae.core.driver.http.dto.region.RegionResponse
import ru.poomsae.core.driver.http.dto.region.UpdateRegionRequest

@Mapper(componentModel = "spring")
interface RegionMapper {
    fun toResponse(region: Region): RegionResponse
    fun fromCreateRequest(request: CreateRegionRequest): Region
    fun fromUpdateRequest(request: UpdateRegionRequest): Region
}