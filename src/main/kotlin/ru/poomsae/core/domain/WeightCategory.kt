package ru.poomsae.core.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class WeightCategory(
    @Id
    val id: Long? = null,
    var name: String,
    var minWeight: Float,
    var maxWeight: Float,
    var gender: String,

    var deleted: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val createdBy: Long = 1,
    var updatedAt: Instant? = null,
    var updatedBy: Long? = null
)
