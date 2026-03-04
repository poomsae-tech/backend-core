package ru.poomsae.core.controller

import org.springframework.web.bind.annotation.*
import ru.poomsae.core.driver.http.dto.organization.CreateOrganizationRequest
import ru.poomsae.core.driver.http.dto.organization.OrganizationResponse
import ru.poomsae.core.driver.http.dto.organization.UpdateOrganizationRequest
import ru.poomsae.core.service.organization.OrganizationService
import ru.poomsae.core.mapper.OrganizationMapper
import ru.poomsae.core.exception.EntityNotFoundException

@RestController
@RequestMapping("/organizations")
class OrganizationController(
    private val service: OrganizationService,
    private val mapper: OrganizationMapper
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): OrganizationResponse =
        service.get(id)
            ?.let { mapper.toResponse(it) }
            ?: throw EntityNotFoundException("Organization $id not found")

    @GetMapping
    fun getMany(): List<OrganizationResponse> =
        service.getMany().map { mapper.toResponse(it) }

    @PostMapping
    fun create(@RequestBody request: CreateOrganizationRequest): OrganizationResponse =
        mapper.toResponse(
            service.create(mapper.fromCreateRequest(request))
        )

    @PutMapping
    fun update(@RequestBody request: UpdateOrganizationRequest): OrganizationResponse =
        mapper.toResponse(
            service.update(mapper.fromUpdateRequest(request))
        )

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}