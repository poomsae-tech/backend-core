package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.poomsae.core.domain.BeltLevel
import ru.poomsae.core.driver.http.dto.requests.beltlevel.CreateBeltLevelRequest
import ru.poomsae.core.driver.http.dto.requests.beltlevel.UpdateBeltLevelRequest
import ru.poomsae.core.driver.http.dto.responses.BeltLevelResponse

@Mapper(componentModel = "spring")
interface BeltLevelMapper {

    fun toResponse(entity: BeltLevel): BeltLevelResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: CreateBeltLevelRequest): BeltLevel

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: UpdateBeltLevelRequest): BeltLevel
}