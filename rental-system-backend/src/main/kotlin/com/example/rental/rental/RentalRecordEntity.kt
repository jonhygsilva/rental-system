package com.example.rental.rental

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "rentals")
data class RentalRecordEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val contratoId: Long,
    val equipamentoId: Long,
    val rentedAt: LocalDate,
    val returnedAt: LocalDate? = null,
    val userId: Long
)
