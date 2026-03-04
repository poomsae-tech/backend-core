package ru.poomsae.core.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController;
import ru.poomsae.core.driver.http.dto.federation.CreateFederationRequest
import ru.poomsae.core.driver.http.dto.federation.FederationResponse
import ru.poomsae.core.driver.http.dto.federation.UpdateFederationRequest
import ru.poomsae.core.exception.EntityNotFoundException
import ru.poomsae.core.mapper.FederationMapper
import ru.poomsae.core.service.federation.FederationService

@RestController
@RequestMapping("/federations")
class FederationController(
    private val service: FederationService,
    private val mapper: FederationMapper
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long) =
            service.get(id)?.let { mapper.toResponse(it) }
            ?: throw EntityNotFoundException("Federation $id not found")

    @GetMapping
    fun getMany(): List<FederationResponse> =
        service.getMany().map { mapper.toResponse(it) }

    @PostMapping
    fun create(@RequestBody request: CreateFederationRequest) =
        mapper.toResponse(
            service.create(mapper.fromCreateRequest(request))
        )

    @PutMapping
    fun update(@RequestBody request: UpdateFederationRequest) =
        mapper.toResponse(
            service.update(mapper.fromUpdateRequest(request))
        )

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}
