package ru.poomsae.core.service.interfaces

import ru.poomsae.core.domain.Organization

interface OrganizationService {
    fun get(id: Long): Organization?

    fun getMany(): List<Organization>

    fun create(organization: Organization): Organization

    fun update(organization: Organization): Organization

    fun delete(id: Long)
}