package ru.poomsae.core.service

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.interfaces.WeightCategoryRepository
import ru.poomsae.core.domain.WeightCategory
import ru.poomsae.core.service.interfaces.WeightCategoryService

@Service
class WeightCategoryServiceImpl (
    private val weightCategoryRepository: WeightCategoryRepository
) : WeightCategoryService {
    override fun get(id: Long): WeightCategory? =
        weightCategoryRepository.get(id)

    override fun getMany(): List<WeightCategory> =
        weightCategoryRepository.getMany()

    override fun create(weightCategory: WeightCategory): WeightCategory =
        weightCategoryRepository.create(weightCategory)

    override fun update(weightCategory: WeightCategory): WeightCategory =
        weightCategoryRepository.update(weightCategory)

    override fun delete(id: Long) =
        weightCategoryRepository.delete(id)
}