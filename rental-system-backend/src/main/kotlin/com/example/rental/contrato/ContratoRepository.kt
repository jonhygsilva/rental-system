package com.example.rental.contrato

import com.example.rental.contrato.domain.ContratoEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ContratoRepository : JpaRepository<ContratoEntity, Long>
