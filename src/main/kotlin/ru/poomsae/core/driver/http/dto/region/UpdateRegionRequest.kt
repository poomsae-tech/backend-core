package ru.poomsae.core.driver.http.dto.region

import ru.poomsae.core.domain.model.Region

data class UpdateRegionRequest(val id: Long, val name: String, val createdBy: String) {
    fun toDomain() = Region(id = id, name = name, createdBy = createdBy)
}
