package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.poomsae.core.domain.AgeGroup
import ru.poomsae.core.driver.http.dto.requests.agegroup.CreateAgeGroupRequest
import ru.poomsae.core.driver.http.dto.requests.agegroup.UpdateAgeGroupRequest
import ru.poomsae.core.driver.http.dto.responses.AgeGroupResponse

@Mapper(componentModel = "spring")
interface AgeGroupMapper {

    fun toResponse(entity: AgeGroup): AgeGroupResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: CreateAgeGroupRequest): AgeGroup

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: UpdateAgeGroupRequest): AgeGroup
}