package ru.poomsae.core.service.organization

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.repository.organization.OrganizationRepository
import ru.poomsae.core.domain.model.Organization

@Service
class OrganizationManager(
    private val repository: OrganizationRepository
) : OrganizationService {

    override fun get(id: Long) = repository.get(id)

    override fun getMany() = repository.getMany()

    override fun create(organization: Organization) =
        repository.create(organization)

    override fun update(organization: Organization) =
        repository.update(organization)

    override fun delete(id: Long) =
        repository.delete(id)
}