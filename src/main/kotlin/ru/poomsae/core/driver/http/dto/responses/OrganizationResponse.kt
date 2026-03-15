package ru.poomsae.core.driver.http.dto.responses

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Ответ с данными клуба")
data class OrganizationResponse(
    @Schema(description = "", example = "")
    val id: Long,
    @Schema(description = "", example = "")
    val name: String,
    @Schema(description = "", example = "")
    val inn: String,
    @Schema(description = "", example = "")
    val address: String,
    @Schema(description = "", example = "")
    val status: Long,
    @Schema(description = "", example = "")
    val federationId: Long?,
    @Schema(description = "", example = "")
    val regionId: Long?,
    @Schema(description = "Флаг удаления", example = "false")
    val deleted: Boolean,
    @Schema(description = "Дата создания", example = "2026-03-15T10:00:00Z")
    val createdAt: Instant,
    @Schema(description = "ID создателя", example = "1")
    val createdBy: Long,
    @Schema(description = "Дата обновления", example = "2026-03-15T12:00:00Z")
    val updatedAt: Instant?,
    @Schema(description = "ID обновившего", example = "1")
    val updatedBy: Long?
)
