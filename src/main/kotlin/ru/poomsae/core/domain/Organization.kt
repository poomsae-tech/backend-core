package ru.poomsae.core.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table
data class Organization(
    @Id
    val id: Long? = null,
    var name: String,
    var inn: String,
    var address: String,
    var status: OrganizationStatus = OrganizationStatus.PENDING,
    var federationId: Long,
    var regionId: Long,

    var deleted: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val createdBy: Long = 1,
    var updatedAt: Instant? = null,
    var updatedBy: Long? = null
)

enum class OrganizationStatus {
    PENDING,
    ACCREDITED,
}