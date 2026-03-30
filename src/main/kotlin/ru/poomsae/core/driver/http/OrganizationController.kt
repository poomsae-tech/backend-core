package ru.poomsae.core.driver.http

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.poomsae.core.domain.Organization
import ru.poomsae.core.driver.http.dto.requests.organization.CreateOrganizationRequest
import ru.poomsae.core.driver.http.dto.requests.organization.UpdateOrganizationRequest
import ru.poomsae.core.driver.http.dto.responses.OrganizationResponse
import ru.poomsae.core.mapper.OrganizationMapper
import ru.poomsae.core.service.interfaces.OrganizationService

@RestController
@RequestMapping("/organizations")
@Tag(name = "Organization", description = "API для управления клубами")
class OrganizationController(
    private val organizationServise: OrganizationService,
    private val organizationMapper: OrganizationMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{id}")
    @Operation(summary = "Получить клуб по ID")
    @ApiResponse(responseCode = "200", description = "Клуб найден")
    @ApiResponse(responseCode = "404", description = "Клуб не найден")
    fun get(
        @Parameter(description = "")
        @PathVariable id: Long
    ): ResponseEntity<OrganizationResponse> {
        val organization = organizationServise.get(id)

        requireNotNull(organization) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.ok(organizationMapper.toResponse(organization))
    }

    @GetMapping
    @Operation(summary = "Получить все клубы")
    @ApiResponse(responseCode = "200", description = "Список клубов")
    fun getMany(): ResponseEntity<List<OrganizationResponse>> {
        val organizations: List<Organization> = organizationServise.getMany()
        val response: List<OrganizationResponse> = organizations.map { organizationMapper.toResponse(it) }
        return ResponseEntity.ok(response)
    }

    @PostMapping
    @Operation(summary = "Создать клуб")
    @ApiResponse(responseCode = "200", description = "Клуб создан")
    fun create(
        @RequestBody request: CreateOrganizationRequest
    ): ResponseEntity<OrganizationResponse> {
        val organization: Organization = organizationServise.create(organizationMapper.toEntity(request))
        return ResponseEntity.ok(organizationMapper.toResponse(organization))
    }

    @PutMapping
    @Operation(summary = "Обновить клуб")
    @ApiResponse(responseCode = "200", description = "Клуб обновлен")
    fun update(
        @RequestBody request: UpdateOrganizationRequest
    ): ResponseEntity<OrganizationResponse> {
        val organization: Organization = organizationServise.update(organizationMapper.toEntity(request))
        return ResponseEntity.ok(organizationMapper.toResponse(organization))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить клуб")
    @ApiResponse(responseCode = "204", description = "Клуб удален")
    fun delete(
        @Parameter(description = "ID клуба")
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        organizationServise.delete(id)
        return ResponseEntity.noContent().build()
    }
}