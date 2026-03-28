package ru.poomsae.core.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("entry")
data class Entry(
    @Id
    val id: Long? = null,
    var tournamentId: Long,
    var coachId: Long,
    var organizationId: Long,
    var status: EntryStatus = EntryStatus.PENDING,
    var rejectionReason: String? = null,
    
    var deleted: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val createdBy: Long = 1,
    var updatedAt: Instant? = null,
    var updatedBy: Long? = null
)

enum class EntryStatus {
    PENDING,
    APPROVED,
    REJECTED
}