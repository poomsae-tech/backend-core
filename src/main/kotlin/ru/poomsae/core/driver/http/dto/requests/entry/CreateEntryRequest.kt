package ru.poomsae.core.driver.http.dto.requests.entry

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class CreateEntryRequest(
    @field:NotNull(message = "ID турнира обязателен")
    val tournamentId: Long,
    
    @field:NotEmpty(message = "Список спортсменов не может быть пустым")
    val athletes: List<EntryAthleteRequest>
)

data class EntryAthleteRequest(
    @field:NotNull(message = "ID спортсмена обязателен")
    val athleteId: Long,
    
    @field:NotEmpty(message = "Весовая категория обязательна")
    val weightCategory: String,
    
    @field:NotEmpty(message = "Возрастная группа обязательна")
    val ageGroup: String
)