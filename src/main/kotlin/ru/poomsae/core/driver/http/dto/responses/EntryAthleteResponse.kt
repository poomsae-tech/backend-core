package ru.poomsae.core.driver.http.dto.responses.entry

data class EntryAthleteResponse(
    val athleteId: Long,
    val weightCategory: String,
    val ageGroup: String
)