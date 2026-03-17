package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.poomsae.core.domain.Region
import ru.poomsae.core.driver.http.dto.requests.region.CreateRegionRequest
import ru.poomsae.core.driver.http.dto.responses.RegionResponse
import ru.poomsae.core.driver.http.dto.requests.region.UpdateRegionRequest

@Mapper(componentModel = "spring")
interface RegionMapper {
    fun toResponse(region: Region): RegionResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    fun toEntity(request: CreateRegionRequest): Region

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: UpdateRegionRequest): Region
}