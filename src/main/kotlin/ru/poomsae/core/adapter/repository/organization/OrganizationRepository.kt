package ru.poomsae.core.adapter.repository.organization

import ru.poomsae.core.domain.model.Organization

interface OrganizationRepository {
    fun get(id: Long): Organization?
    fun getMany(): List<Organization>
    fun create(organization: Organization): Organization
    fun update(organization: Organization): Organization
    fun delete(id: Long)
}