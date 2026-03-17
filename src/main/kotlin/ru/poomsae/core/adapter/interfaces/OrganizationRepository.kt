package ru.poomsae.core.adapter.interfaces

import ru.poomsae.core.domain.Organization

interface OrganizationRepository {
    fun get(id: Long): Organization?

    fun getMany(): List<Organization>

    fun create(organization: Organization): Organization

    fun update(organization: Organization): Organization

    fun delete(id: Long)
}