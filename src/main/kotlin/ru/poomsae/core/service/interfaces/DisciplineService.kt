package ru.poomsae.core.service.interfaces

import ru.poomsae.core.domain.Discipline

interface DisciplineService {
    fun get(id: Long): Discipline?

    fun getMany(): List<Discipline>

    fun create(discipline: Discipline): Discipline

    fun update(discipline: Discipline): Discipline

    fun delete(id: Long)
}