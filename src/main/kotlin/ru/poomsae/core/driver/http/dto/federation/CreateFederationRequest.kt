package ru.poomsae.core.driver.http.dto.federation

import ru.poomsae.core.domain.model.Federation
import ru.poomsae.core.domain.model.FederationType

data class CreateFederationRequest(val name: String, val type: FederationType, val createdBy: String) {
    fun toDomain() = Federation(name = name, type = type, createdBy = createdBy)
}

