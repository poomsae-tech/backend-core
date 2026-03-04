package ru.poomsae.core.controller

import org.springframework.web.bind.annotation.*
import ru.poomsae.core.driver.http.dto.region.CreateRegionRequest
import ru.poomsae.core.driver.http.dto.region.RegionResponse
import ru.poomsae.core.driver.http.dto.region.UpdateRegionRequest
import ru.poomsae.core.service.region.RegionService
import ru.poomsae.core.mapper.RegionMapper
import ru.poomsae.core.exception.EntityNotFoundException

@RestController
@RequestMapping("/regions")
class RegionController(
    private val service: RegionService,
    private val mapper: RegionMapper
) {

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): RegionResponse =
        service.get(id)
            ?.let { mapper.toResponse(it) }
            ?: throw EntityNotFoundException("Region $id not found")

    @GetMapping
    fun getMany(): List<RegionResponse> =
        service.getMany().map { mapper.toResponse(it) }

    @PostMapping
    fun create(@RequestBody request: CreateRegionRequest): RegionResponse =
        mapper.toResponse(
            service.create(mapper.fromCreateRequest(request))
        )

    @PutMapping
    fun update(@RequestBody request: UpdateRegionRequest): RegionResponse =
        mapper.toResponse(
            service.update(mapper.fromUpdateRequest(request))
        )

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}