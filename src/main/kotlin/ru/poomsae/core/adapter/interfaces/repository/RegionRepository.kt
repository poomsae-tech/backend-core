package ru.poomsae.core.adapter.interfaces.repository

import ru.poomsae.core.domain.Region

interface RegionRepository {
    fun get(id: Long): Region?

    fun getMany(): List<Region>

    fun create(region: Region): Region

    fun update(region: Region): Region

    fun delete(id: Long)
}