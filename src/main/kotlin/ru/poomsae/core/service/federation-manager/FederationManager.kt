package ru.poomsae.core

import org.springframework.stereotype.Service

@Service
class FederationManager(private val federationRepo: FederationRepo) : FederationService {
  override fun get(id: Long): Federation? {
    return federationRepo.get(id)
  }

  override fun getMany(): List<Federation> {
    return federationRepo.getMany()
  }

  override fun create(federation: Federation): Federation {
    return federationRepo.create(federation)
  }

  override fun update(federation: Federation): Federation {
    return federationRepo.update(federation)
  }

  override fun delete(id: Long) {
    federationRepo.delete(id)
  }
}
