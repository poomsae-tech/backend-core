package ru.poomsae.core.service.interfaces

import ru.poomsae.core.domain.Region

interface RegionService {
    fun get(id: Long): Region?

    fun getMany(): List<Region>

    fun create(region: Region): Region

    fun update(region: Region): Region

    fun delete(id: Long)
}