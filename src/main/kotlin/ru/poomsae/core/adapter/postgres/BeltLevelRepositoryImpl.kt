package ru.poomsae.core.adapter.postgres

import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.poomsae.core.adapter.interfaces.BeltLevelRepository
import ru.poomsae.core.domain.BeltLevel
import java.time.Instant

@Repository
class BeltLevelRepositoryImpl (
    private val db: JdbcTemplate
) : BeltLevelRepository {

    private val rowMapper = DataClassRowMapper(BeltLevel::class.java)

    override fun get(id: Long): BeltLevel? {
        return db.query(
            """
            SELECT id,
                   name,
                   created_at AS createdAt,
                   created_by AS createdBy,
                   updated_at AS updatedAt,
                   updated_by AS updatedBy,
                   deleted
            FROM belt_levels
            WHERE id = ? AND deleted = false
            """.trimIndent(),
            rowMapper,
            id
        ).firstOrNull()
    }

    override fun getMany(): List<BeltLevel> {
        return db.query(
            """
            SELECT id,
                   name,
                   created_at AS createdAt,
                   created_by AS createdBy,
                   updated_at AS updatedAt,
                   updated_by AS updatedBy,
                   deleted
            FROM belt_levels
            WHERE deleted = false
            """.trimIndent(),
            rowMapper
        )
    }

    override fun create(beltLevel: BeltLevel): BeltLevel {
        val created = beltLevel.copy(
            createdAt = Instant.now()
        )

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        db.update({ conn ->
            val ps = conn.prepareStatement(
                """
                INSERT INTO belt_levels (
                    name,
                    created_at,
                    created_by,
                    deleted
                ) VALUES (?, ?, ?, ?)
                """,
                arrayOf("id")
            )
            ps.setString(1, created.name)
            ps.setTimestamp(2, java.sql.Timestamp.from(created.createdAt))
            ps.setLong(3, created.createdBy)
            ps.setBoolean(4, created.deleted)
            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toLong()
        return created.copy(id = generatedId)
    }

    override fun update(beltLevel: BeltLevel): BeltLevel {
        val updated = beltLevel.copy(
            updatedAt = Instant.now(),
            updatedBy = 1 // TODO: заменить на текущего пользователя
        )

        db.update(
            """
            UPDATE belt_levels
            SET name = ?,
                updated_at = NOW(),
                updated_by = ?,
                deleted = ?
            WHERE id = ?
            """.trimIndent(),
            updated.name,
            updated.updatedBy,
            updated.deleted,
            updated.id
        )

        return updated
    }

    override fun delete(id: Long) {
        db.update(
            """
            UPDATE belt_levels
            SET deleted = true,
                updated_at = NOW(),
                updated_by = ?
            WHERE id = ?
            """.trimIndent(),
            1, // TODO: заменить на реального пользователя
            id
        )
    }
}