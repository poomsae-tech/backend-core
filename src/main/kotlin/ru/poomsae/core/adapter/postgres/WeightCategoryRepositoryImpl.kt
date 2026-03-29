package ru.poomsae.core.adapter.postgres

import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.poomsae.core.adapter.interfaces.WeightCategoryRepository
import ru.poomsae.core.domain.WeightCategory
import java.sql.Timestamp
import java.time.Instant

@Repository
class WeightCategoryRepositoryImpl (
    private val db: JdbcTemplate
) : WeightCategoryRepository {

    private val rowMapper = DataClassRowMapper(WeightCategory::class.java)

    override fun get(id: Long): WeightCategory? {
        return db.query(
            """
            SELECT id,
                   name,
                   min_weight AS minWeight,
                   max_weight AS maxWeight,
                   gender,
                   deleted,
                   created_at AS createdAt,
                   created_by AS createdBy,
                   updated_at AS updatedAt,
                   updated_by AS updatedBy
            FROM weight_categories
            WHERE id = ? AND deleted = false
            """.trimIndent(),
            rowMapper,
            id
        ).firstOrNull()
    }

    override fun getMany(): List<WeightCategory> {
        return db.query(
            """
            SELECT id,
                   name,
                   min_weight AS minWeight,
                   max_weight AS maxWeight,
                   gender,
                   deleted,
                   created_at AS createdAt,
                   created_by AS createdBy,
                   updated_at AS updatedAt,
                   updated_by AS updatedBy
            FROM weight_categories
            WHERE deleted = false
            """.trimIndent(),
            rowMapper
        )
    }

    override fun create(weightCategory: WeightCategory): WeightCategory {
        val created = weightCategory.copy(
            createdAt = Instant.now()
        )

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        db.update({ conn ->
            val ps = conn.prepareStatement(
                """
                INSERT INTO weight_categories (
                    name,
                    min_weight,
                    max_weight,
                    gender,
                    created_at,
                    created_by,
                    deleted
                ) VALUES (?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                arrayOf("id")
            )

            ps.setString(1, created.name)
            ps.setFloat(2, created.minWeight)
            ps.setFloat(3, created.maxWeight)
            ps.setString(4, created.gender)
            ps.setTimestamp(5, Timestamp.from(created.createdAt))
            ps.setLong(6, created.createdBy)
            ps.setBoolean(7, created.deleted)

            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toLong()

        return created.copy(id = generatedId)
    }

    override fun update(weightCategory: WeightCategory): WeightCategory {
        val updated = weightCategory.copy(
            updatedAt = Instant.now(),
            updatedBy = 1 // TODO: заменить на текущего пользователя
        )

        db.update(
            """
            UPDATE weight_categories
            SET name = ?,
                min_weight = ?,
                max_weight = ?,
                gender = ?,
                updated_at = NOW(),
                updated_by = ?,
                deleted = ?
            WHERE id = ?
            """.trimIndent(),
            updated.name,
            updated.minWeight,
            updated.maxWeight,
            updated.gender,
            updated.updatedBy,
            updated.deleted,
            updated.id
        )

        return updated
    }

    override fun delete(id: Long) {
        db.update(
            """
            UPDATE weight_categories
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