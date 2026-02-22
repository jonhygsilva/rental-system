package com.example.rental.contrato

import com.example.rental.contrato.domain.ContratoEntity
import org.springframework.stereotype.Service

@Service
class ContratoService(private val repo: ContratoRepository) {
    fun listByUser(userId: Long) = repo.findAll().filter { it.userId == userId }
    fun create(c: ContratoEntity) = repo.save(c)
}
