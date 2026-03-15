package ru.poomsae.core.adapter.postgres

import java.time.Instant
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.poomsae.core.domain.Federation

@Repository
class FederationRepositoryImpl(
    private val db: JdbcTemplate
) : FederationRepository {

  private val rowMapper = BeanPropertyRowMapper(Federation::class.java)

  override fun get(id: Long): Federation? {
    return db.query(
            """
                SELECT
                  id,
                  name,
                  created_at,
                  created_by,
                  updated_at,
                  updated_by,
                  deleted
                FROM federations
                WHERE id = ?
                AND deleted = false""".trimIndent(),
            rowMapper,
            id,
        )
        .firstOrNull()
  }

  override fun getMany(): List<Federation> {
    return db.query(
        """
            SELECT
              id,
              name,
              created_at,
              created_by,
              updated_at,
              updated_by,
              deleted
            FROM federations
            WHERE deleted = false""".trimIndent(),
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
              created_at,
              created_by,
              deleted
            ) VALUES (
              ?,
              ?,
              ?,
              ?
            )""",
            arrayOf("id")
        )
        ps.setString(1, createdFederation.name)
        ps.setTimestamp(2, java.sql.Timestamp.from(createdFederation.createdAt))
        ps.setLong(3, createdFederation.createdBy)
        ps.setBoolean(4, createdFederation.deleted)
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
              updated_at = NOW(), 
              updated_by = ?, 
              deleted = ? 
            WHERE id = ?""".trimIndent(),
        updatedFederation.name,
        updatedFederation.updatedBy,
        updatedFederation.deleted,
        updatedFederation.id,
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
            WHERE id = ?""",
        1, // TODO: заменить на реального пользователя
        id
    )
  }
}
