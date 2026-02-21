package ru.poomsae.core

import org.springframework.stereotype.Service

@Service
class FederationManager(private val federationRepo: FederationRepo) : FederationService {
  override fun Get(id: Long): Federation? {
    return federationRepo.Get(id)
  }

  override fun GetMany(): List<Federation> {
    return federationRepo.GetMany()
  }

  override fun Create(federation: Federation): Federation {
    return federationRepo.Create(federation)
  }

  override fun Update(federation: Federation): Federation {
    return federationRepo.Update(federation)
  }

  override fun Delete(id: Long) {
    federationRepo.Delete(id)
  }
}
