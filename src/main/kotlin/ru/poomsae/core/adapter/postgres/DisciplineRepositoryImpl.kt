package ru.poomsae.core.adapter.postgres

import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.poomsae.core.adapter.interfaces.DisciplineRepository
import ru.poomsae.core.domain.Discipline
import java.sql.Timestamp
import java.time.Instant

@Repository
class DisciplineRepositoryImpl (
    private val db: JdbcTemplate
) : DisciplineRepository {

    private val rowMapper = DataClassRowMapper(Discipline::class.java)

    override fun get(id: Long): Discipline? {
        return db.query(
            """
            SELECT id,
                   name,
                   created_at AS createdAt,
                   created_by AS createdBy,
                   updated_at AS updatedAt,
                   updated_by AS updatedBy,
                   deleted
            FROM disciplines
            WHERE id = ? AND deleted = false
            """.trimIndent(),
            rowMapper,
            id
        ).firstOrNull()
    }

    override fun getMany(): List<Discipline> {
        return db.query(
            """
            SELECT id,
                   name,
                   created_at AS createdAt,
                   created_by AS createdBy,
                   updated_at AS updatedAt,
                   updated_by AS updatedBy,
                   deleted
            FROM disciplines
            WHERE deleted = false
            """.trimIndent(),
            rowMapper
        )
    }

    override fun create(discipline: Discipline): Discipline {
        val createdDiscipline =
            discipline.copy(
                createdAt = Instant.now()
            )

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        db.update({ conn ->
            val ps = conn.prepareStatement(
                """
                INSERT INTO disciplines (
                    name,
                    created_at,
                    created_by,
                    deleted
                ) VALUES (?, ?, ?, ?)
                """.trimIndent(),
                arrayOf("id")
            )

            ps.setString(1, createdDiscipline.name)
            ps.setTimestamp(2, Timestamp.from(createdDiscipline.createdAt))
            ps.setLong(3, createdDiscipline.createdBy)
            ps.setBoolean(4, createdDiscipline.deleted)

            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toLong()

        return createdDiscipline.copy(id = generatedId)
    }

    override fun update(discipline: Discipline): Discipline {
        val updatedDiscipline =
            discipline.copy(
                updatedAt = Instant.now(),
                updatedBy = 1 // TODO: заменить на текущего пользователя
            )

        db.update(
            """
            UPDATE disciplines
            SET name = ?,
                updated_at = NOW(),
                updated_by = ?
            WHERE id = ?
            """.trimIndent(),
            updatedDiscipline.name,
            updatedDiscipline.updatedBy,
            updatedDiscipline.id
        )

        return updatedDiscipline
    }

    override fun delete(id: Long) {
        db.update(
            """
            UPDATE disciplines
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