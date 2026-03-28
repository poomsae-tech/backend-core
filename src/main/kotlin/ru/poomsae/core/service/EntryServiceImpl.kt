package ru.poomsae.core.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.poomsae.core.adapter.interfaces.EntryRepository
import ru.poomsae.core.domain.Entry
import ru.poomsae.core.domain.EntryAthlete
import ru.poomsae.core.domain.EntryStatus
import ru.poomsae.core.driver.http.dto.requests.entry.CreateEntryRequest
import ru.poomsae.core.service.interfaces.EntryService
import java.time.Instant

@Service
class EntryServiceImpl(
    private val entryRepository: EntryRepository
    // TODO: Добавить при наличии:
    // private val athleteRepository: AthleteRepository,
    // private val tournamentRepository: TournamentRepository,
    // private val userRepository: UserRepository,
) : EntryService {
    
    @Transactional
    override fun createEntry(
        request: CreateEntryRequest,
        coachId: Long,
        organizationId: Long
    ): Entry {
        // === ВАЛИДАЦИИ (заглушки до появления репозиториев) ===
        
        // TODO: Проверить роль пользователя (COACH)
        // TODO: Проверить существование турнира
        // TODO: Проверить, что приём заявок ещё открыт
        // TODO: Проверить, что спортсмены принадлежат организации тренера
        // TODO: Проверить возраст, пол, пояс, вес спортсменов
        // TODO: Проверить действующую страховку
        
        // === СОЗДАНИЕ ЗАЯВКИ ===
        val entry = entryRepository.create(
            Entry(
                tournamentId = request.tournamentId,
                coachId = coachId,
                organizationId = organizationId,
                status = EntryStatus.PENDING
            )
        )
        
        // === СОЗДАНИЕ ЗАПИСЕЙ СПОРТСМЕНОВ ===
        request.athletes.forEach { athleteReq ->
            entryRepository.createAthlete(
                EntryAthlete(
                    entryId = entry.id!!,
                    athleteId = athleteReq.athleteId,
                    weightCategory = athleteReq.weightCategory,
                    ageGroup = athleteReq.ageGroup
                )
            )
        }
        
        return entry
    }
    
    @Transactional(readOnly = true)
    override fun getEntriesByTournament(
        tournamentId: Long,
        status: EntryStatus?
    ): List<Entry> {
        return entryRepository.getMany(tournamentId, status)
    }
    
    @Transactional
    override fun updateEntryStatus(
        entryId: Long,
        newStatus: EntryStatus,
        rejectionReason: String?
    ): Entry {
        val entry = entryRepository.get(entryId)
            ?: throw IllegalArgumentException("Заявка не найдена")
        
        entry.status = newStatus
        entry.rejectionReason = rejectionReason
        entry.updatedAt = Instant.now()
        
        return entryRepository.update(entry)
    }
    /*
    fun getAthletesByEntryId(entryId: Long): List<EntryAthlete> {
        // TODO: реализовать, когда будет EntryAthleteRepository
        return emptyList()
    }
    */
}