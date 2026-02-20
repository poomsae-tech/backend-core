package ru.poomsae.core

data class Club(
    val id: Long,
    val federation_id: Long,
    var name: String,
    var is_accredited: Boolean,
    var is_blocked: Boolean,
)
