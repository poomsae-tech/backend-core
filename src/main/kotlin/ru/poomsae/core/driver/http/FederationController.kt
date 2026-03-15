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
import ru.poomsae.core.domain.Federation
import ru.poomsae.core.driver.http.dto.requests.federation.CreateFederationRequest
import ru.poomsae.core.driver.http.dto.requests.federation.UpdateFederationRequest
import ru.poomsae.core.driver.http.dto.responses.FederationResponse
import ru.poomsae.core.mapper.FederationMapper
import ru.poomsae.core.service.interfaces.FederationService

@RestController
@RequestMapping("/federations")
@Tag(name = "Federation", description = "API для управления федерациями")
class FederationController(
    private val federationService: FederationService,
    private val federationMapper: FederationMapper
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/{federation_id}")
    @Operation(summary = "Получить федерацию по ID")
    @ApiResponse(responseCode = "200", description = "Федерация найдена")
    @ApiResponse(responseCode = "404", description = "Федерация не найдена")
    fun get(
      @Parameter(description = "ID федерации")
      @PathVariable federation_id: Long
    ): ResponseEntity<FederationResponse> {
      val federation = federationService.get(federation_id)

      requireNotNull(federation) {
        return ResponseEntity.notFound().build()
      }

      return ResponseEntity.ok(federationMapper.toResponse(federation))
    }

    @GetMapping
    @Operation(summary = "Получить все федерации")
    @ApiResponse(responseCode = "200", description = "Список федераций")
    fun getMany(): ResponseEntity<List<FederationResponse>> {
      val federations: List<Federation> = federationService.getMany()
      val response: List<FederationResponse> = federations.map { federationMapper.toResponse(it) }
      return ResponseEntity.ok(response)
    }

    @PostMapping
    @Operation(summary = "Создать федерацию")
    @ApiResponse(responseCode = "200", description = "Федерация создана")
    fun create(
      @RequestBody request: CreateFederationRequest
    ): ResponseEntity<FederationResponse> {
      val federation: Federation = federationService.create(federationMapper.toEntity(request))
      return ResponseEntity.ok(federationMapper.toResponse(federation))
    }

    @PutMapping
    @Operation(summary = "Обновить федерацию")
    @ApiResponse(responseCode = "200", description = "Федерация обновлена")
    fun update(
      @RequestBody request: UpdateFederationRequest
    ): ResponseEntity<FederationResponse> {
      val federation: Federation = federationService.update(federationMapper.toEntity(request))
      return ResponseEntity.ok(federationMapper.toResponse(federation))
    }

    @DeleteMapping("/{federation_id}")
    @Operation(summary = "Удалить федерацию")
    @ApiResponse(responseCode = "204", description = "Федерация удалена")
    fun delete(
      @Parameter(description = "ID федерации")
      @PathVariable federation_id: Long
    ): ResponseEntity<Unit> {
      federationService.delete(federation_id)
      return ResponseEntity.noContent().build()
    }
}
