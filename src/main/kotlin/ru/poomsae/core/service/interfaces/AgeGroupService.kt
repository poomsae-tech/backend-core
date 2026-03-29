package ru.poomsae.core.service.interfaces

import ru.poomsae.core.domain.AgeGroup

interface AgeGroupService {
    fun get(id: Long): AgeGroup?

    fun getMany(): List<AgeGroup>

    fun create(ageGroup: AgeGroup): AgeGroup

    fun update(ageGroup: AgeGroup): AgeGroup

    fun delete(id: Long)
}