package ru.poomsae.core.driver.http.dto.responses

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(description = "Ответ с данными уровня пояса")
data class BeltLevelResponse(

    @Schema(description = "ID уровня пояса", example = "1")
    val id: Long,

    @Schema(description = "Название уровня пояса", example = "Чёрный 1 Дан")
    val name: String,

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