package ru.poomsae.core.adapter.interfaces

import ru.poomsae.core.domain.BeltLevel

interface BeltLevelRepository {
    fun get(id: Long): BeltLevel?

    fun getMany(): List<BeltLevel>

    fun create(beltLevel: BeltLevel): BeltLevel

    fun update(beltLevel: BeltLevel): BeltLevel

    fun delete(id: Long)
}