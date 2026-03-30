package ru.poomsae.core.adapter.postgres

import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.poomsae.core.adapter.interfaces.AgeGroupRepository
import ru.poomsae.core.domain.AgeGroup
import java.time.Instant

@Repository
class AgeGroupRepositoryImpl(
    private val db: JdbcTemplate
) : AgeGroupRepository {

    private val rowMapper = DataClassRowMapper(AgeGroup::class.java)

    override fun get(id: Long): AgeGroup? {
        return db.query(
            """
            SELECT id,
                   name,
                   min_age    AS minAge,
                   max_age    AS maxAge,
                   created_at AS createdAt,
                   created_by AS createdBy,
                   updated_at AS updatedAt,
                   updated_by AS updatedBy,
                   deleted
            FROM age_groups
            WHERE id = ? AND deleted = false
            """.trimIndent(),
            rowMapper,
            id
        ).firstOrNull()
    }

    override fun getMany(): List<AgeGroup> {
        return db.query(
            """
            SELECT id,
                   name,
                   min_age    AS minAge,
                   max_age    AS maxAge,
                   created_at AS createdAt,
                   created_by AS createdBy,
                   updated_at AS updatedAt,
                   updated_by AS updatedBy,
                   deleted
            FROM age_groups
            WHERE deleted = false
            """.trimIndent(),
            rowMapper
        )
    }

    override fun create(ageGroup: AgeGroup): AgeGroup {
        val createdAgeGroup = ageGroup.copy(
            createdAt = Instant.now()
        )

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        db.update({ conn ->
            val ps = conn.prepareStatement(
                """
                INSERT INTO age_groups (
                    name,
                    min_age,
                    max_age,
                    created_at,
                    created_by,
                    deleted
                ) VALUES (?, ?, ?, ?, ?, ?)
                """,
                arrayOf("id")
            )
            ps.setString(1, createdAgeGroup.name)
            ps.setInt(2, createdAgeGroup.minAge)
            ps.setInt(3, createdAgeGroup.maxAge)
            ps.setTimestamp(4, java.sql.Timestamp.from(createdAgeGroup.createdAt))
            ps.setLong(5, createdAgeGroup.createdBy)
            ps.setBoolean(6, createdAgeGroup.deleted)
            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toLong()
        return createdAgeGroup.copy(id = generatedId)
    }

    override fun update(ageGroup: AgeGroup): AgeGroup {
        val updatedAgeGroup = ageGroup.copy(
            updatedAt = Instant.now(),
            updatedBy = 1 // TODO: заменить на текущего пользователя
        )

        db.update(
            """
            UPDATE age_groups
            SET name = ?,
                min_age = ?,
                max_age = ?,
                updated_at = NOW(),
                updated_by = ?,
                deleted = ?
            WHERE id = ?
            """.trimIndent(),
            updatedAgeGroup.name,
            updatedAgeGroup.minAge,
            updatedAgeGroup.maxAge,
            updatedAgeGroup.updatedBy,
            updatedAgeGroup.deleted,
            updatedAgeGroup.id
        )

        return updatedAgeGroup
    }

    override fun delete(id: Long) {
        db.update(
            """
            UPDATE age_groups
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