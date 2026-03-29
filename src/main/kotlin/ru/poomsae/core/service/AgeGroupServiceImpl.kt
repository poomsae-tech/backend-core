package ru.poomsae.core.service

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.interfaces.AgeGroupRepository
import ru.poomsae.core.domain.AgeGroup
import ru.poomsae.core.service.interfaces.AgeGroupService

@Service
class AgeGroupServiceImpl (
    private val ageGroupRepository: AgeGroupRepository
) : AgeGroupService {
    override fun get(id: Long): AgeGroup? =
        ageGroupRepository.get(id)

    override fun getMany(): List<AgeGroup> =
        ageGroupRepository.getMany()

    override fun create(ageGroup: AgeGroup): AgeGroup =
        ageGroupRepository.create(ageGroup)

    override fun update(ageGroup: AgeGroup): AgeGroup =
        ageGroupRepository.update(ageGroup)

    override fun delete(id: Long) =
        ageGroupRepository.delete(id)
}