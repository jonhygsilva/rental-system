package com.example.rental.customer.adapter.outbound.persistence.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * JPA entity — infrastructure concern only.
 * Maps to the "addresses" table.
 */
@Entity
@Table(name = "addresses")
data class AddressJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val street: String = "",
    val number: String = "",
    val complement: String? = null,
    val neighborhood: String = "",
    val city: String = "",
    val state: String = "",
    val zipCode: String = "",
    val customerId: Long = 0
)