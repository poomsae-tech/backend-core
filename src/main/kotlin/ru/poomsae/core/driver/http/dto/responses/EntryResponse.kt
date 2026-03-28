package ru.poomsae.core.driver.http.dto.responses.entry

import ru.poomsae.core.driver.http.dto.responses.entry.EntryAthleteResponse

import ru.poomsae.core.domain.EntryStatus
import java.time.Instant

data class EntryResponse(
    val id: Long,
    val tournamentId: Long,
    val coachId: Long,
    val organizationId: Long,
    val status: EntryStatus,
    val rejectionReason: String?,
    val athletes: List<EntryAthleteResponse>,
    val createdAt: Instant
)

