package ru.poomsae.core

interface FederationService {
  fun Get(id: Long): Federation?

  fun GetMany(): List<Federation>

  fun Create(federation: Federation): Federation

  fun Update(federation: Federation): Federation

  fun Delete(id: Long)
}
