package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.poomsae.core.domain.Discipline
import ru.poomsae.core.driver.http.dto.requests.discipline.CreateDisciplineRequest
import ru.poomsae.core.driver.http.dto.requests.discipline.UpdateDisciplineRequest
import ru.poomsae.core.driver.http.dto.responses.DisciplineResponse

@Mapper(componentModel = "spring")
interface DisciplineMapper {

    fun toResponse(entity: Discipline): DisciplineResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: CreateDisciplineRequest): Discipline

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: UpdateDisciplineRequest): Discipline
}