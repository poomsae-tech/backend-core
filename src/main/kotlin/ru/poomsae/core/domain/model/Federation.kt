package ru.poomsae.core.domain.model

import java.time.LocalDateTime

enum class FederationType {
    ALL_RUSSIAN,
    REGIONAL
}

data class Federation(
    val id: Long? = null,
    var name: String,
    var regionId: Long? = null,
    var type: FederationType,

    val createdBy: String? = null,
    val createdAt: LocalDateTime? = null,
    var updatedBy: String? = null,
    var updatedAt: LocalDateTime? = null,
    var deleted: Boolean = false
)