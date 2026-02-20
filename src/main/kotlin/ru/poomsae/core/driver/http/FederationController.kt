package ru.poomsae.core

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/federations")
class FederationController(private val federationService: FederationService) {
  @GetMapping("/{federation_id}")
  fun Get(@PathVariable federation_id: Long): ResponseEntity<FederationResponse> {
    var federation = federationService.Get(federation_id)
    return ResponseEntity.ok(federation.toResponse())
  }

  @GetMapping("/")
  fun GetMany(): ResponseEntity<List<FederationResponse>> {
    var federations: List<Federation> = federationService.GetMany()
    var response: List<FederationResponse> = federations.map { it.toResponse() }
    return ResponseEntity.ok(response)
  }

  @PostMapping("/")
  fun Create(@RequestBody request: CreateFederationRequest): ResponseEntity<FederationResponse> {
    var federation = federationService.Create(request.toDomain())
    return ResponseEntity.ok(federation.toResponse())
  }

  @PutMapping("/")
  fun Update(@RequestBody request: UpdateFederationRequest): ResponseEntity<FederationResponse> {
    var federation = federationService.Update(request.toDomain())
    return ResponseEntity.ok(federation.toResponse())
  }

  @DeleteMapping("/{federation_id}")
  fun Delete(@PathVariable federation_id: Long): ResponseEntity<Void> {
    federationService.Delete(federation_id)
    return ResponseEntity.noContent().build()
  }
}
