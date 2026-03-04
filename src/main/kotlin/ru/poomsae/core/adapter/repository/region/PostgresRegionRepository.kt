package ru.poomsae.core.adapter.repository.region

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.stereotype.Repository
import ru.poomsae.core.domain.model.Region

@Repository
class PostgresRegionRepository(
    private val db: JdbcTemplate
) : RegionRepository {

    private val rowMapper = DataClassRowMapper(Region::class.java)

    override fun get(id: Long): Region? =
        db.query(
            "SELECT * FROM regions WHERE id = ? AND deleted = false",
            rowMapper,
            id
        ).firstOrNull()

    override fun getMany(): List<Region> =
        db.query(
            "SELECT * FROM regions WHERE deleted = false",
            rowMapper
        )

    override fun create(r: Region): Region {
        val sql = """
            INSERT INTO regions (name, created_by, created_at, deleted)
            VALUES (?, ?, NOW(), false)
            RETURNING *
        """
        return db.queryForObject(sql, rowMapper, r.name, r.createdBy)!!
    }

    override fun update(r: Region): Region {
        val sql = """
            UPDATE regions
            SET name = ?, updated_by = ?, updated_at = NOW()
            WHERE id = ? AND deleted = false
            RETURNING *
        """
        return db.queryForObject(sql, rowMapper, r.name, r.updatedBy, r.id)!!
    }

    override fun delete(id: Long) {
        db.update(
            "UPDATE regions SET deleted = true, updated_at = NOW() WHERE id = ?",
            id
        )
    }
}