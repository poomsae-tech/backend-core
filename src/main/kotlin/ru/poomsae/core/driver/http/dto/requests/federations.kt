package ru.poomsae.core.driver.http.dto.requests

data class CreateFederationRequest(val name: String)

data class UpdateFederationRequest(val id: Long, val name: String, val deleted: Boolean)
