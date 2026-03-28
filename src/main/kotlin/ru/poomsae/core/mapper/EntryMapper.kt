package ru.poomsae.core.mapper

import org.springframework.stereotype.Component
import ru.poomsae.core.domain.Entry
import ru.poomsae.core.domain.EntryAthlete
import ru.poomsae.core.driver.http.dto.requests.entry.CreateEntryRequest
import ru.poomsae.core.driver.http.dto.responses.entry.EntryResponse
import ru.poomsae.core.driver.http.dto.responses.entry.EntryAthleteResponse

@Component
class EntryMapper {
    
    // Временная заглушка — организация и тренер устанавливаются в сервисе
    fun toEntity(request: CreateEntryRequest): Entry {
        return Entry(
            tournamentId = request.tournamentId,
            coachId = 0L, // будет установлено в сервисе
            organizationId = 0L // будет установлено в сервисе
        )
    }
    
    fun toResponse(entry: Entry, athletes: List<EntryAthlete> = emptyList()): EntryResponse {
        return EntryResponse(
            id = entry.id ?: throw IllegalStateException("Entry ID is null"),
            tournamentId = entry.tournamentId,
            coachId = entry.coachId,
            organizationId = entry.organizationId,
            status = entry.status,
            rejectionReason = entry.rejectionReason,
            athletes = athletes.map { toAthleteResponse(it) },
            createdAt = entry.createdAt
        )
    }
    
    fun toAthleteResponse(athlete: EntryAthlete): EntryAthleteResponse {
        return EntryAthleteResponse(
            athleteId = athlete.athleteId,
            weightCategory = athlete.weightCategory,
            ageGroup = athlete.ageGroup
        )
    }
}