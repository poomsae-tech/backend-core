package ru.poomsae.core.driver.http.dto.requests.entry

import ru.poomsae.core.domain.EntryStatus

data class UpdateEntryStatusRequest(
    val status: EntryStatus,
    val rejectionReason: String? = null
)