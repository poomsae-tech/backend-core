package ru.poomsae.core.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class AgeGroup(
    @Id
    val id: Long? = null,
    var name: String,
    var minAge: Int,
    var maxAge: Int,

    var deleted: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val createdBy: Long = 1,
    var updatedAt: Instant? = null,
    var updatedBy: Long? = null
)
