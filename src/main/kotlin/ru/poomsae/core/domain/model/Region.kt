package ru.poomsae.core.domain.model

import java.time.LocalDateTime

data class Region(
    val id: Long? = null,
    var name: String,

    val createdBy: String? = null,
    val createdAt: LocalDateTime? = null,
    var updatedBy: String? = null,
    var updatedAt: LocalDateTime? = null,
    var deleted: Boolean = false
)