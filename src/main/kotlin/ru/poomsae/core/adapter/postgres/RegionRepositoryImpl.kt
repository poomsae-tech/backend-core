package ru.poomsae.core.adapter.repository.region

import java.time.Instant
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.poomsae.core.adapter.interfaces.repository.RegionRepository
import ru.poomsae.core.domain.Region

@Repository
class PostgresRegionRepository(
    private val db: JdbcTemplate
) : RegionRepository {

    private val rowMapper = BeanPropertyRowMapper(Region::class.java)

    override fun get(id: Long): Region? {
        return db.query(
            """
            SELECT id,
                   name,
                   deleted,
                   created_at AS createdAt,
                   created_by AS createdBy,
                   updated_at AS updatedAt,
                   updated_by AS updatedBy
            FROM regions
            WHERE id = ? AND deleted = false
            """.trimIndent(),
            rowMapper,
            id,
        ).firstOrNull()
    }

    override fun getMany(): List<Region> {
        return db.query(
            """
                SELECT id,
                       name,
                       deleted,
                       created_at AS createdAt,
                       created_by AS createdBy,
                       updated_at AS updatedAt,
                       updated_by AS updatedBy
                FROM regions
                WHERE deleted = false
            """.trimIndent(),
            rowMapper
        )
    }

    override fun create(region: Region): Region {
        val createdRegion =
            region.copy(
                createdAt = Instant.now(),
            )

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        db.update({ conn ->
            val ps = conn.prepareStatement(
            """
                INSERT INTO regions (
                            name,
                            created_at,
                            created_by,
                            deleted
                ) VALUES (?, ?, ?, ?)
                """,
            arrayOf("id"))

            ps.setString(1, createdRegion.name)
            ps.setTimestamp(2, java.sql.Timestamp.from(createdRegion.createdAt))
            ps.setLong(3, createdRegion.createdBy)
            ps.setBoolean(4, createdRegion.deleted)
            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toLong()
        return createdRegion.copy(id = generatedId)
    }

    override fun update(region: Region): Region {
        val updatedRegion =
            region.copy(
                updatedAt = Instant.now(),
                updatedBy = 1, // TODO: заменить на реального пользователя
            )

        db.update(
            """
            UPDATE regions
            SET name = ?,
                updated_at = NOW(),
                updated_by = ?,
                deleted = ?
            WHERE id = ?
            """.trimIndent(),
            updatedRegion.name,
            updatedRegion.updatedBy,
            updatedRegion.deleted,
            updatedRegion.id,
        )

        return updatedRegion
    }

    override fun delete(id: Long) {
        db.update(
            """
                UPDATE regions
                SET deleted = true,
                    updated_at = NOW(),
                    updated_by = ?
                WHERE id = ?
                """,
            1,
            id
        )
    }
}