package ru.poomsae.core

data class CreateFederationRequest(val name: String) {
  fun toDomain(): Federation {
    return Federation(name = this.name)
  }
}

data class UpdateFederationRequest(val id: Long, val name: String, val deleted: Boolean) {
  fun toDomain(): Federation {
    return Federation(id = this.id, name = this.name, deleted = this.deleted)
  }
}
