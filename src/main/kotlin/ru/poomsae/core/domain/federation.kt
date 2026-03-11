package ru.poomsae.core

data class Federation(
    val id: Long? = null,
    var name: String,
    var deleted: Boolean = false,
    val createdAt: java.time.Instant = java.time.Instant.now(),
    val createdBy: Long = 1,
    var updatedAt: java.time.Instant? = null,
    var updatedBy: Long? = null
)
