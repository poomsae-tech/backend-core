package ru.poomsae.core.service

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.interfaces.repository.OrganizationRepository
import ru.poomsae.core.domain.Organization
import ru.poomsae.core.service.interfaces.OrganizationService

@Service
class OrganizationServiceImpl(
    private val organizationRepo: OrganizationRepository
) : OrganizationService {

    override fun get(id: Long) =
        organizationRepo.get(id)

    override fun getMany() =
        organizationRepo.getMany()

    override fun create(organization: Organization) =
        organizationRepo.create(organization)

    override fun update(organization: Organization) =
        organizationRepo.update(organization)

    override fun delete(id: Long) =
        organizationRepo.delete(id)
}