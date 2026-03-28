package ru.poomsae.core.adapter.postgres

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import ru.poomsae.core.adapter.interfaces.EntryRepository
import ru.poomsae.core.domain.Entry
import ru.poomsae.core.domain.EntryAthlete
import ru.poomsae.core.domain.EntryStatus
import java.sql.ResultSet
import java.time.Instant

@Repository
class EntryRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : EntryRepository {
    
    // === RowMapper для Entry ===
    private val entryRowMapper = RowMapper<Entry> { rs: ResultSet, _: Int ->
        Entry(
            id = rs.getLong("id"),
            tournamentId = rs.getLong("tournament_id"),
            coachId = rs.getLong("coach_id"),
            organizationId = rs.getLong("organization_id"),
            status = EntryStatus.valueOf(rs.getString("status")),
            rejectionReason = rs.getString("rejection_reason"),
            deleted = rs.getBoolean("deleted"),
            createdAt = rs.getTimestamp("created_at")?.toInstant() ?: Instant.now(),
            createdBy = rs.getLong("created_by"),
            updatedAt = rs.getTimestamp("updated_at")?.toInstant(),
            updatedBy = if (rs.getLong("updated_by") == 0L) null else rs.getLong("updated_by")
        )
    }
    
    // === RowMapper для EntryAthlete ===
    private val entryAthleteRowMapper = RowMapper<EntryAthlete> { rs: ResultSet, _: Int ->
        EntryAthlete(
            id = rs.getLong("id"),
            entryId = rs.getLong("entry_id"),
            athleteId = rs.getLong("athlete_id"),
            weightCategory = rs.getString("weight_category"),
            ageGroup = rs.getString("age_group"),
            deleted = rs.getBoolean("deleted"),
            createdAt = rs.getTimestamp("created_at")?.toInstant() ?: Instant.now(),
            createdBy = rs.getLong("created_by"),
            updatedAt = rs.getTimestamp("updated_at")?.toInstant(),
            updatedBy = if (rs.getLong("updated_by") == 0L) null else rs.getLong("updated_by")
        )
    }
    
    override fun get(id: Long): Entry? {
        val sql = "SELECT * FROM entry WHERE id = ? AND deleted = false"
        return jdbcTemplate.queryForObject(sql, entryRowMapper, id)
    }
    
    override fun getMany(tournamentId: Long, status: EntryStatus?): List<Entry> {
        val sql = buildString {
            append("SELECT * FROM entry WHERE tournament_id = ? AND deleted = false")
            if (status != null) {
                append(" AND status = ?")
            }
            append(" ORDER BY created_at DESC")
        }
        return if (status != null) {
            jdbcTemplate.query(sql, entryRowMapper, tournamentId, status.name)
        } else {
            jdbcTemplate.query(sql, entryRowMapper, tournamentId)
        }
    }
    
    override fun create(entry: Entry): Entry {
        val sql = """
            INSERT INTO entry (
                tournament_id, coach_id, organization_id, status, rejection_reason,
                created_at, created_by
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """.trimIndent()
        
        val id = jdbcTemplate.queryForObject(
            sql, Long::class.java,
            entry.tournamentId,
            entry.coachId,
            entry.organizationId,
            entry.status.name,
            entry.rejectionReason,
            entry.createdAt,
            entry.createdBy
        )
        return entry.copy(id = id)
    }
    
    override fun update(entry: Entry): Entry {
        val sql = """
            UPDATE entry SET
                status = ?,
                rejection_reason = ?,
                updated_at = ?,
                updated_by = ?
            WHERE id = ?
            RETURNING *
        """.trimIndent()
        
        return jdbcTemplate.queryForObject(
            sql, entryRowMapper,
            entry.status.name,
            entry.rejectionReason,
            entry.updatedAt ?: Instant.now(),
            entry.updatedBy,
            entry.id
        )!!
    }
    
    override fun delete(id: Long) {
        // Soft delete: помечаем как удалённый
        val sql = "UPDATE entry SET deleted = true, updated_at = ? WHERE id = ?"
        jdbcTemplate.update(sql, Instant.now(), id)
    }
    
    // === Методы для EntryAthlete ===
    
    override fun getAthletesByEntryId(entryId: Long): List<EntryAthlete> {
        val sql = "SELECT * FROM entry_athlete WHERE entry_id = ? AND deleted = false"
        return jdbcTemplate.query(sql, entryAthleteRowMapper, entryId)
    }
    
    override fun createAthlete(entryAthlete: EntryAthlete): EntryAthlete {
        val sql = """
            INSERT INTO entry_athlete (
                entry_id, athlete_id, weight_category, age_group,
                created_at, created_by
            ) VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id
        """.trimIndent()
        
        val id = jdbcTemplate.queryForObject(
            sql, Long::class.java,
            entryAthlete.entryId,
            entryAthlete.athleteId,
            entryAthlete.weightCategory,
            entryAthlete.ageGroup,
            entryAthlete.createdAt,
            entryAthlete.createdBy
        )
        return entryAthlete.copy(id = id)
    }
}