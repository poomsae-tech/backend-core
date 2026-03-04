package ru.poomsae.core.driver.http.dto.federation

import ru.poomsae.core.domain.model.Federation
import ru.poomsae.core.domain.model.FederationType

data class UpdateFederationRequest(val id: Long, val name: String, val type: FederationType, val updatedBy: String) {
    fun toDomain() = Federation(id = id, name = name, type = type, updatedBy = updatedBy)
}


