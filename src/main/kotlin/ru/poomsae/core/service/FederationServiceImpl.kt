package ru.poomsae.core.service

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.interfaces.FederationRepository
import ru.poomsae.core.domain.Federation
import ru.poomsae.core.service.interfaces.FederationService

@Service
class FederationServiceImpl(
  private val federationRepo: FederationRepository
) : FederationService {
    override fun get(id: Long): Federation? =
      federationRepo.get(id)

    override fun getMany(): List<Federation> =
      federationRepo.getMany()

    override fun create(federation: Federation): Federation =
       federationRepo.create(federation)

    override fun update(federation: Federation): Federation =
      federationRepo.update(federation)

    override fun delete(id: Long) =
      federationRepo.delete(id)
}