package ru.poomsae.core

interface FederationRepo {
  fun get(id: Long): Federation?

  fun getMany(): List<Federation>

  fun create(federation: Federation): Federation

  fun update(federation: Federation): Federation

  fun delete(id: Long)
}
