package ru.poomsae.core.service

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.interfaces.BeltLevelRepository
import ru.poomsae.core.domain.BeltLevel
import ru.poomsae.core.service.interfaces.BeltLevelService

@Service
class BeltLevelServiceImpl (
    private val beltLevelRepository: BeltLevelRepository
) : BeltLevelService {
    override fun get(id: Long): BeltLevel? =
        beltLevelRepository.get(id)

    override fun getMany(): List<BeltLevel> =
        beltLevelRepository.getMany()

    override fun create(beltLevel: BeltLevel): BeltLevel =
        beltLevelRepository.create(beltLevel)

    override fun update(beltLevel: BeltLevel): BeltLevel =
        beltLevelRepository.update(beltLevel)

    override fun delete(id: Long) =
        beltLevelRepository.delete(id)
}