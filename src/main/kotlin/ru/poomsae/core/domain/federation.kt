package ru.poomsae.core

data class Federation(val id: Long? = null, var name: String) {
  fun toResponse(): FederationResponse {
    requireNotNull(this.id) {
      "Cannot create FederationResponse for unsaved Federation (id is null)"
    }
    return FederationResponse(this.id, this.name)
  }
}
