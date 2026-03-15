package ru.poomsae.core.adapter.postgres

import ru.poomsae.core.domain.Federation

interface FederationRepository {
  fun get(id: Long): Federation?

  fun getMany(): List<Federation>

  fun create(federation: Federation): Federation

  fun update(federation: Federation): Federation

  fun delete(id: Long)
}