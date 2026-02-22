package com.example.rental.customer.adapter.outbound.persistence.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * JPA entity — infrastructure concern only.
 * Maps to the "clientes" table.
 */
@Entity
@Table(name = "clientes")
data class CustomerJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String = "",
    val document: String = "",
    val phone: String = "",
    val userId: Long = 0
)