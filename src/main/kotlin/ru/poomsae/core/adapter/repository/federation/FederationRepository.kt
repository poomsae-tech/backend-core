package ru.poomsae.core.adapter.repository.federation

import ru.poomsae.core.domain.model.Federation

interface FederationRepository {
    fun get(id: Long): Federation?
    fun getMany(): List<Federation>
    fun create(federation: Federation): Federation
    fun update(federation: Federation): Federation
    fun delete(id: Long): Boolean
}