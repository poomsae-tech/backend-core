package ru.poomsae.core

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
import ru.poomsae.core.driver.http.dto.requests.CreateFederationRequest
import ru.poomsae.core.driver.http.dto.requests.UpdateFederationRequest
import ru.poomsae.core.driver.http.dto.responses.FederationResponse
import ru.poomsae.core.mapper.FederationMapper

@RestController
@RequestMapping("/federations")
class FederationController(private val federationService: FederationService) {
  private val log = LoggerFactory.getLogger(this::class.java)

  @GetMapping("/{federation_id}")
  fun get(@PathVariable federation_id: Long): ResponseEntity<FederationResponse> {
    var federation = federationService.get(federation_id)

    requireNotNull(federation) {
      return ResponseEntity.notFound().build()
    }

    return ResponseEntity.ok(FederationMapper.INSTANCE.toResponse(federation))
  }

  @GetMapping
  fun getMany(): ResponseEntity<List<FederationResponse>> {
    var federations: List<Federation> = federationService.getMany()
    var response: List<FederationResponse> = federations.map { FederationMapper.INSTANCE.toResponse(it) }
    return ResponseEntity.ok(response)
  }

  @PostMapping
  fun create(@RequestBody request: CreateFederationRequest): ResponseEntity<FederationResponse> {
    var federation = federationService.create(FederationMapper.INSTANCE.toEntity(request))
    return ResponseEntity.ok(FederationMapper.INSTANCE.toResponse(federation))
  }

  @PutMapping
  fun update(@RequestBody request: UpdateFederationRequest): ResponseEntity<FederationResponse> {
    var federation = federationService.update(FederationMapper.INSTANCE.toEntity(request))
    return ResponseEntity.ok(FederationMapper.INSTANCE.toResponse(federation))
  }

  @DeleteMapping("/{federation_id}")
  fun delete(@PathVariable federation_id: Long): ResponseEntity<Void> {
    federationService.delete(federation_id)
    return ResponseEntity.noContent().build()
  }
}
