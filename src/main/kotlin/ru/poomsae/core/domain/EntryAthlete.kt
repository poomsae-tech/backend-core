package ru.poomsae.core.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("entry_athlete")
data class EntryAthlete(
    @Id
    val id: Long? = null,
    var entryId: Long,
    var athleteId: Long,
    var weightCategory: String,
    var ageGroup: String,
    
    var deleted: Boolean = false,
    val createdAt: Instant = Instant.now(),
    val createdBy: Long = 1,
    var updatedAt: Instant? = null,
    var updatedBy: Long? = null
)