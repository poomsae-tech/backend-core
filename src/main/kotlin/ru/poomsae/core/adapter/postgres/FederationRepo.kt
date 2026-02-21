package ru.poomsae.core

import java.sql.ResultSet
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository

@Repository
class PostgresFederationRepo(private val db: JdbcTemplate) : FederationRepo {

  private val rowMapper =
      RowMapper<Federation> { rs: ResultSet, _ ->
        Federation(id = rs.getLong("id"), name = rs.getString("name"))
      }

  override fun Get(id: Long): Federation? {
    return db.query(
            "SELECT id, name FROM federations WEHRE id = ?",
            rowMapper,
            id,
        )
        .firstOrNull()
  }

  override fun GetMany(): List<Federation> {
    return db.query(
        "SELECT id, name FROM federations",
        rowMapper,
    )
  }

  override fun Create(federation: Federation): Federation {
    db.update(
        "INSERT INTO federations (name) VALUES (?)",
        federation.name,
    )
    return federation
  }

  override fun Update(federation: Federation): Federation {
    db.update(
        "UPDATE federations SET name = ? WHERE id = ?",
        federation.name,
        federation.id,
    )
    return federation
  }

  override fun Delete(id: Long) {
    db.update("DELETE FROM federations WHERE id = ?", id)
  }
}
