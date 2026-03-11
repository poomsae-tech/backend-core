package ru.poomsae.core

data class FederationResponse(
    val id: Long,
    val name: String,
    val deleted: Boolean,
    val createdAt: java.time.Instant,
    val createdBy: Long,
    val updatedAt: java.time.Instant?,
    val updatedBy: Long?
)
