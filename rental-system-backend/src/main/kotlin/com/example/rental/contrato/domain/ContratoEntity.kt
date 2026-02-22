package com.example.rental.contrato.domain

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "contratos")
data class ContratoEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val customerId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val totalValue: BigDecimal,
    val paid: Boolean,
    val userId: Long
)
