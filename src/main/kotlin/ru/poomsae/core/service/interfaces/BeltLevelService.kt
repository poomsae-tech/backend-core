package ru.poomsae.core.service.interfaces

import ru.poomsae.core.domain.BeltLevel

interface BeltLevelService {
    fun get(id: Long): BeltLevel?

    fun getMany(): List<BeltLevel>

    fun create(beltLevel: BeltLevel): BeltLevel

    fun update(beltLevel: BeltLevel): BeltLevel

    fun delete(id: Long)
}