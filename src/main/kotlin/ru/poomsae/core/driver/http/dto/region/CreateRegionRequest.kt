package ru.poomsae.core.driver.http.dto.region

import ru.poomsae.core.domain.model.Region

data class CreateRegionRequest(val name: String, val createdBy: String) {
    fun toDomain() = Region(name = name, createdBy = createdBy)
}
