package ru.poomsae.core.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import ru.poomsae.core.domain.WeightCategory
import ru.poomsae.core.driver.http.dto.requests.weightcategory.CreateWeightCategoryRequest
import ru.poomsae.core.driver.http.dto.requests.weightcategory.UpdateWeightCategoryRequest
import ru.poomsae.core.driver.http.dto.responses.WeightCategoryResponse

@Mapper(componentModel = "spring")
interface WeightCategoryMapper {

    fun toResponse(entity: WeightCategory): WeightCategoryResponse

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: CreateWeightCategoryRequest): WeightCategory

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    fun toEntity(request: UpdateWeightCategoryRequest): WeightCategory
}