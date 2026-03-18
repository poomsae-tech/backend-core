package ru.poomsae.core.driver.http.dto.responses

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Ответ с данными клуба")
data class OrganizationResponse(
    @Schema(description = "ID клуба", example = "1")
    val id: Long,
    @Schema(description = "Название клуба", example = "Спортивный клуб тхэквондо 'ITF Master'")
    val name: String,
    @Schema(description = "ИНН клуба", example = "7701123456")
    val inn: String,
    @Schema(description = "Адрес клуба", example = "г. Москва, ул. Спортивная, д. 10, стр. 2")
    val address: String,
    @Schema(description = "Статус клуба", example = "PENDING")
    val status: String,
    @Schema(description = "ID федерации", example = "1")
    val federationId: Long,
    @Schema(description = "ID региона", example = "1")
    val regionId: Long,
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
