package ru.poomsae.core.adapter.repository.federation

import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import ru.poomsae.core.domain.model.Federation

@Repository
class PostgresFederationRepository(private val db: JdbcTemplate) : FederationRepository {
    private val rowMapper = DataClassRowMapper(Federation::class.java)

    override fun get(id: Long) =
        db.query("SELECT * FROM federations WHERE id = ? AND deleted = false", rowMapper, id).firstOrNull()

    override fun getMany() =
        db.query("SELECT * FROM federations WHERE deleted = false", rowMapper)

    override fun create(federation: Federation) =
        db.queryForObject(
            "INSERT INTO federations (name, region_id, type, created_by, created_at, deleted) VALUES (?, ?, ?, ?, NOW(), false) RETURNING *",
            rowMapper, federation.name, federation.regionId, federation.type, federation.createdBy
        )

    override fun update(federation: Federation) =
        db.queryForObject(
            "UPDATE federations SET name = ?, region_id = ?, type = ?, updated_by = ?, updated_at = NOW() WHERE id = ? AND deleted = false RETURNING *",
            rowMapper, federation.name, federation.regionId, federation.type, federation.updatedBy, federation.id
        )

    override fun delete(id: Long): Boolean {
        val updated = db.update(
            "UPDATE federations SET deleted = true, updated_at = NOW() WHERE id = ?",
            id
        )
        return updated > 0
    }
}