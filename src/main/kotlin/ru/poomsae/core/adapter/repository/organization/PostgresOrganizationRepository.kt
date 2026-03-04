package ru.poomsae.core.adapter.repository.organization

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.stereotype.Repository
import ru.poomsae.core.domain.model.Organization

@Repository
class PostgresOrganizationRepository(
    private val db: JdbcTemplate
) : OrganizationRepository {

    private val rowMapper = DataClassRowMapper(Organization::class.java)

    override fun get(id: Long): Organization? =
        db.query(
            "SELECT * FROM organizations WHERE id = ? AND deleted = false",
            rowMapper,
            id
        ).firstOrNull()

    override fun getMany(): List<Organization> =
        db.query(
            "SELECT * FROM organizations WHERE deleted = false",
            rowMapper
        )

    override fun create(organization: Organization): Organization {
        val sql = """
            INSERT INTO organizations (name, created_by, created_at, deleted)
            VALUES (?, ?, NOW(), false)
            RETURNING *
        """
        return db.queryForObject(sql, rowMapper, organization.name, organization.createdBy)
    }

    override fun update(organization: Organization): Organization {
        val sql = """
            UPDATE organizations
            SET name = ?, updated_by = ?, updated_at = NOW()
            WHERE id = ? AND deleted = false
            RETURNING *
        """
        return db.queryForObject(sql, rowMapper, organization.name, organization.updatedBy, organization.id)
    }

    override fun delete(id: Long) {
        db.update(
            "UPDATE organizations SET deleted = true, updated_at = NOW() WHERE id = ?",
            id
        )
    }
}