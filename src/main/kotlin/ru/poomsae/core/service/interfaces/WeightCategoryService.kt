package ru.poomsae.core.service.interfaces

import ru.poomsae.core.domain.WeightCategory

interface WeightCategoryService {
    fun get(id: Long): WeightCategory?

    fun getMany(): List<WeightCategory>

    fun create(weightCategory: WeightCategory): WeightCategory

    fun update(weightCategory: WeightCategory): WeightCategory

    fun delete(id: Long)
}