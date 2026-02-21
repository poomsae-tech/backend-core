package ru.poomsae.core

data class CreateFederationRequest(val name: String) {
  fun toDomain(): Federation {
    return Federation(name = this.name)
  }
}

data class UpdateFederationRequest(val name: String) {
  fun toDomain(): Federation {
    return Federation(name = this.name)
  }
}
