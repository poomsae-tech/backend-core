package ru.poomsae.core.adapter.postgres

import java.time.Instant
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import ru.poomsae.core.adapter.interfaces.OrganizationRepository
import ru.poomsae.core.domain.Organization

@Repository
class OrganizationRepositoryImpl(
    private val db: JdbcTemplate
) : OrganizationRepository {

    private val rowMapper = DataClassRowMapper(Organization::class.java)

    override fun get(id: Long): Organization? {
        return db.query(
            """
                SELECT id,
                       name,
                       inn,
                       address,
                       status::text      AS status,
                       federation_id     AS federationId,
                       region_id         AS regionId,
                       deleted,
                       created_at        AS createdAt,
                       created_by        AS createdBy,
                       updated_at        AS updatedAt,
                       updated_by        AS updatedBy
                FROM organizations
                WHERE id = ? AND deleted = false
                """.trimIndent(),
            rowMapper,
            id
        ).firstOrNull()
    }

    override fun getMany(): List<Organization> {
        return db.query(
            """
                SELECT id,
                       name,
                       inn,
                       address,
                       status::text      AS status,
                       federation_id     AS federationId,
                       region_id         AS regionId,
                       deleted,
                       created_at        AS createdAt,
                       created_by        AS createdBy,
                       updated_at        AS updatedAt,
                       updated_by        AS updatedBy
                FROM organizations
                WHERE deleted = false
                """.trimIndent(),
            rowMapper
        )
    }

    override fun create(organization: Organization): Organization {

        val createdOrganization =
            organization.copy(
                createdAt = Instant.now(),
            )

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        db.update({ conn ->
            val ps = conn.prepareStatement(
                """
                INSERT INTO organizations (
                            name,
                            inn,
                            address,
                            status,
                            federation_id,
                            region_id,
                            created_at,
                            created_by,
                            deleted
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                arrayOf("id")
            )
            ps.setString(1, createdOrganization.name)
            ps.setString(2, createdOrganization.inn)
            ps.setString(3, createdOrganization.address)
            ps.setString(4, createdOrganization.status.name)
            ps.setLong(5, createdOrganization.federationId)
            ps.setLong(6, createdOrganization.regionId)
            ps.setTimestamp(7, java.sql.Timestamp.from(createdOrganization.createdAt))
            ps.setLong(8, createdOrganization.createdBy)
            ps.setBoolean(9, createdOrganization.deleted)
            ps
        }, keyHolder)

        val generatedId = keyHolder.key?.toLong()
        return createdOrganization.copy(id = generatedId)
    }

    override fun update(organization: Organization): Organization {
        val updateOrganization =
            organization.copy(
                updatedAt = Instant.now(),
                updatedBy = 1,
            )

        db.update("""
            UPDATE organizations
            SET name = ?,
                inn = ?,
                address = ?,
                status = ?,
                federation_id = ?,
                region_id = ?,
                updated_at = NOW(),
                updated_by = ?,
                deleted = ?
            WHERE id = ?
            """.trimIndent(),
            updateOrganization.name,
            updateOrganization.inn,
            updateOrganization.address,
            updateOrganization.status.name,
            updateOrganization.federationId,
            updateOrganization.regionId,
            updateOrganization.updatedBy,
            updateOrganization.deleted,
            updateOrganization.id
        )

        return updateOrganization
    }

    override fun delete(id: Long) {
        db.update(
            """
                UPDATE organizations
                SET deleted = true,
                    updated_at = NOW(),
                    updated_by = ?
                WHERE id = ?
                """.trimIndent(),
            1,
            id
        )
    }
}