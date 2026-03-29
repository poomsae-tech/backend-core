package ru.poomsae.core.adapter.interfaces

import ru.poomsae.core.domain.AgeGroup

interface AgeGroupRepository {
    fun get(id: Long): AgeGroup?

    fun getMany(): List<AgeGroup>

    fun create(ageGroup: AgeGroup): AgeGroup

    fun update(ageGroup: AgeGroup): AgeGroup

    fun delete(id: Long)
}