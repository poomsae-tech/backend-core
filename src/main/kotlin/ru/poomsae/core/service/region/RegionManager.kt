package ru.poomsae.core.service.region

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.repository.region.RegionRepository
import ru.poomsae.core.domain.model.Region

@Service
class RegionManager(
    private val repository: RegionRepository
) : RegionService {

    override fun get(id: Long) = repository.get(id)

    override fun getMany() = repository.getMany()

    override fun create(region: Region) =
        repository.create(region)

    override fun update(region: Region) =
        repository.update(region)

    override fun delete(id: Long) =
        repository.delete(id)
}