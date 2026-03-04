package ru.poomsae.core.service.organization

import ru.poomsae.core.domain.model.Organization

interface OrganizationService {
    fun get(id: Long): Organization?
    fun getMany(): List<Organization>
    fun create(organization: Organization): Organization
    fun update(organization: Organization): Organization
    fun delete(id: Long)
}