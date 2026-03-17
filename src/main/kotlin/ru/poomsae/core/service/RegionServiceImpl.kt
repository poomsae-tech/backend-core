package ru.poomsae.core.service

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.interfaces.RegionRepository
import ru.poomsae.core.domain.Region
import ru.poomsae.core.service.interfaces.RegionService

@Service
class RegionServiceImpl(
    private val regionRepo: RegionRepository
) : RegionService {

    override fun get(id: Long) =
        regionRepo.get(id)

    override fun getMany() =
        regionRepo.getMany()

    override fun create(region: Region) =
        regionRepo.create(region)

    override fun update(region: Region) =
        regionRepo.update(region)

    override fun delete(id: Long) =
        regionRepo.delete(id)
}