package ru.poomsae.core.adapter.interfaces

import ru.poomsae.core.domain.Entry
import ru.poomsae.core.domain.EntryAthlete
import ru.poomsae.core.domain.EntryStatus

interface EntryRepository {
    fun get(id: Long): Entry?
    fun getMany(tournamentId: Long, status: EntryStatus? = null): List<Entry>
    fun create(entry: Entry): Entry
    fun update(entry: Entry): Entry
    fun delete(id: Long)
    
    // Спортсмены в заявке
    fun getAthletesByEntryId(entryId: Long): List<EntryAthlete>
    fun createAthlete(entryAthlete: EntryAthlete): EntryAthlete
}