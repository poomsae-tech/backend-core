package ru.poomsae.core.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class Federation(
    @Id
    val id: Long? = null,
    var name: String,
    var regionId: Long,
    var federationType: FederationType,

    var deleted: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val createdBy: Long = 1,
    var updatedAt: Instant? = null,
    var updatedBy: Long? = null
)

enum class FederationType {
    ALL_RUSSIAN,
    REGIONAL
}