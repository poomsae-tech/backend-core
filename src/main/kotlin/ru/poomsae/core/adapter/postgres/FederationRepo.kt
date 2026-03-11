package ru.poomsae.core

import java.time.Instant
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PostgresFederationRepo(private val db: JdbcTemplate) : FederationRepo {

  private val rowMapper = BeanPropertyRowMapper(Federation::class.java)

  override fun get(id: Long): Federation? {
    return db.query(
            "SELECT id, name, created_at, created_by, updated_at, updated_by, deleted FROM federations WHERE id = ? AND deleted = false",
            rowMapper,
            id,
        )
        .firstOrNull()
  }

  override fun getMany(): List<Federation> {
    return db.query(
        "SELECT id, name, created_at, created_by, updated_at, updated_by, deleted FROM federations WHERE deleted = false",
        rowMapper,
    )
  }

  override fun create(federation: Federation): Federation {
    db.update(
        "INSERT INTO federations (name, created_by, deleted) VALUES (?, ?, ?)",
        federation.name,
        federation.createdBy,
        federation.deleted,
    )
    return federation
  }

  override fun update(federation: Federation): Federation {
    val updatedFederation =
        federation.copy(
            updatedAt = Instant.now(),
            updatedBy = 1, // TODO: заменить на реального пользователя
        )
    
    db.update(
        "UPDATE federations SET name = ?, updated_at = NOW(), updated_by = ?, deleted = ? WHERE id = ?",
        updatedFederation.name,
        updatedFederation.updatedBy,
        updatedFederation.deleted,
        updatedFederation.id,
    )
    return updatedFederation
  }

  override fun delete(id: Long) {
    db.update(
        "UPDATE federations SET deleted = true, updated_at = NOW(), updated_by = ? WHERE id = ?",
        1, // TODO: заменить на реального пользователя
        id
    )
  }
}
