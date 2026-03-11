package ru.poomsae.core

data class Federation(
    val id: Long? = null,
    var name: String,
    var deleted: Boolean = false,
    val createdAt: java.time.Instant = java.time.Instant.now(),
    val createdBy: Long = 1,
    var updatedAt: java.time.Instant? = null,
    var updatedBy: Long? = null
) {
  fun toResponse(): FederationResponse {
    requireNotNull(this.id) {
      "Cannot create FederationResponse for unsaved Federation (id is null)"
    }
    return FederationResponse(this.id, this.name, this.deleted, this.createdAt, this.createdBy, this.updatedAt, this.updatedBy)
  }
}
