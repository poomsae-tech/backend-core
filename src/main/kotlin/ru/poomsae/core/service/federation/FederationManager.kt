package ru.poomsae.core.service.federation

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.repository.federation.FederationRepository
import ru.poomsae.core.domain.model.Federation

@Service
class FederationManager(
    private val repository: FederationRepository
) : FederationService {
    override fun get(id: Long) = repository.get(id)

    override fun getMany() = repository.getMany()

    override fun create(federation: Federation) =
        repository.create(federation)

    override fun update(federation: Federation) =
        repository.update(federation)

    override fun delete(id: Long) = repository.delete(id)
}