package ru.poomsae.core.domain.model

import java.time.LocalDateTime

enum class OrganizationStatus {
    PENDING,
    ACCREDITED
}

data class Organization(
    val id: Long? = null,
    var name: String,
    var inn: String,
    var address: String,
    var status: OrganizationStatus,
    var federationId: Long? = null,
    var regionId: Long? = null,

    val createdBy: String? = null,
    val createdAt: LocalDateTime? = null,
    var updatedBy: String? = null,
    var updatedAt: LocalDateTime? = null,
    var deleted: Boolean = false
)