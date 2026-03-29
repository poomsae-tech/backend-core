package ru.poomsae.core.adapter.interfaces

import ru.poomsae.core.domain.Discipline

interface DisciplineRepository {
    fun get(id: Long): Discipline?

    fun getMany(): List<Discipline>

    fun create(discipline: Discipline): Discipline

    fun update(discipline: Discipline): Discipline

    fun delete(id: Long)
}