package ru.poomsae.core.adapter.postgres

import java.time.Instant
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.poomsae.core.adapter.interfaces.repository.FederationRepository
import ru.poomsae.core.domain.Federation

@Repository
class FederationRepositoryImpl(
    private val db: JdbcTemplate
) : FederationRepository {

  private val rowMapper = BeanPropertyRowMapper(Federation::class.java)

  override fun get(id: Long): Federation? {
    return db.query(
            """
                SELECT id,
                       name,
                       region_id             AS regionId,
                       federation_type::text AS federationType,
                       created_at            AS createdAt,
                       created_by            AS createdBy,
                       updated_at            AS updatedAt,
                       updated_by            AS updatedBy,
                       deleted
                FROM federations
                WHERE id = ? AND deleted = false
                """.trimIndent(),
            rowMapper,
            id,
        ).firstOrNull()
  }

  override fun getMany(): List<Federation> {
    return db.query(
        """
            SELECT id,
                   name,
                   region_id             AS regionId,
                   federation_type::text AS federationType,
                   created_at            AS createdAt,
                   created_by            AS createdBy,
                   updated_at            AS updatedAt,
                   updated_by            AS updatedBy,
                   deleted
            FROM federations
            WHERE deleted = false
            """.trimIndent(),
        rowMapper,
    )
  }

  override fun create(federation: Federation): Federation {
    val createdFederation =
        federation.copy(
            createdAt = Instant.now(),
        )

    val keyHolder: KeyHolder = GeneratedKeyHolder()

    db.update({ conn ->
        val ps = conn.prepareStatement(
            """
            INSERT INTO federations (
                        name,
                        region_id,
                        federation_type,
                        created_at,
                        created_by,
                        deleted
            ) VALUES (?, ?, ?, ?, ?, ?)
            """,
            arrayOf("id")
        )
        ps.setString(1, createdFederation.name)
        ps.setLong(2, createdFederation.regionId)
        ps.setString(3, createdFederation.federationType.name)
        ps.setTimestamp(4, java.sql.Timestamp.from(createdFederation.createdAt))
        ps.setLong(5, createdFederation.createdBy)
        ps.setBoolean(6, createdFederation.deleted)
        ps
    }, keyHolder)

    val generatedId = keyHolder.key?.toLong()
    return createdFederation.copy(id = generatedId)
  }

  override fun update(federation: Federation): Federation {
    val updatedFederation =
        federation.copy(
            updatedAt = Instant.now(),
            updatedBy = 1, // TODO: заменить на реального пользователя
        )
    
    db.update(
        """
            UPDATE federations
            SET name = ?,
                region_id = ?,
                federation_type = ?,
                updated_at = NOW(),
                updated_by = ?,
                deleted = ?
            WHERE id = ?
            """.trimIndent(),
        updatedFederation.name,
        updatedFederation.regionId,
        updatedFederation.federationType.name,
        updatedFederation.updatedBy,
        updatedFederation.deleted,
        updatedFederation.id
    )

    return updatedFederation
  }

  override fun delete(id: Long) {
    db.update(
        """
            UPDATE federations
            SET deleted = true,
                updated_at = NOW(),
                updated_by = ?
            WHERE id = ?
            """,
        1, // TODO: заменить на реального пользователя
        id
    )
  }
}
