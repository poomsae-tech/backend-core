package ru.poomsae.core.adapter.interfaces

import ru.poomsae.core.domain.WeightCategory

interface WeightCategoryRepository {
    fun get(id: Long): WeightCategory?

    fun getMany(): List<WeightCategory>

    fun create(weightCategory: WeightCategory): WeightCategory

    fun update(weightCategory: WeightCategory): WeightCategory

    fun delete(id: Long)
}