package ru.poomsae.core.service

import org.springframework.stereotype.Service
import ru.poomsae.core.adapter.interfaces.DisciplineRepository
import ru.poomsae.core.domain.Discipline
import ru.poomsae.core.service.interfaces.DisciplineService

@Service
class DisciplineServiceImpl (
    private val disciplineRepository: DisciplineRepository
) : DisciplineService {
    override fun get(id: Long): Discipline? =
        disciplineRepository.get(id)

    override fun getMany(): List<Discipline> =
        disciplineRepository.getMany()

    override fun create(discipline: Discipline): Discipline =
        disciplineRepository.create(discipline)

    override fun update(discipline: Discipline): Discipline =
        disciplineRepository.update(discipline)

    override fun delete(id: Long) =
        disciplineRepository.delete(id)
}