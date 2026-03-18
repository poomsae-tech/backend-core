package ru.poomsae.core.service.interfaces

import ru.poomsae.core.domain.Federation

interface FederationService {
  fun get(id: Long): Federation?

  fun getMany(): List<Federation>

  fun create(federation: Federation): Federation

  fun update(federation: Federation): Federation

  fun delete(id: Long)
}