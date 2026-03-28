package ru.poomsae.core.service.interfaces

import ru.poomsae.core.domain.Entry
import ru.poomsae.core.domain.EntryStatus
import ru.poomsae.core.driver.http.dto.requests.entry.CreateEntryRequest

interface EntryService {
    fun createEntry(request: CreateEntryRequest, coachId: Long, organizationId: Long): Entry
    fun getEntriesByTournament(tournamentId: Long, status: EntryStatus? = null): List<Entry>
    fun updateEntryStatus(entryId: Long, newStatus: EntryStatus, rejectionReason: String?): Entry
}