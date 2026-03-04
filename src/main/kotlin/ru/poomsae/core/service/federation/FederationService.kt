package ru.poomsae.core.service.federation

import ru.poomsae.core.domain.model.Federation

interface FederationService {
    fun get(id: Long): Federation?
    fun getMany(): List<Federation>
    fun create(federation: Federation): Federation
    fun update(federation: Federation): Federation
    fun delete(id: Long): Boolean
}