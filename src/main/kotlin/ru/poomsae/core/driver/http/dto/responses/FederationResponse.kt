package ru.poomsae.core.driver.http.dto.responses

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Ответ с данными федерации")
data class FederationResponse(
    @Schema(description = "ID федерации", example = "1")
    val id: Long,
    @Schema(description = "Название федерации", example = "Российская федерация тхэквондо")
    val name: String,
    @Schema(description = "Тип федерации", example = "ALL_RUSSIAN")
    val federationType: String,
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
