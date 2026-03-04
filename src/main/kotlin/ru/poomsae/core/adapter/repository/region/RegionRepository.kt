package ru.poomsae.core.adapter.repository.region

import ru.poomsae.core.domain.model.Region

interface RegionRepository {
    fun get(id: Long): Region?
    fun getMany(): List<Region>
    fun create(region: Region): Region
    fun update(region: Region): Region
    fun delete(id: Long)
}