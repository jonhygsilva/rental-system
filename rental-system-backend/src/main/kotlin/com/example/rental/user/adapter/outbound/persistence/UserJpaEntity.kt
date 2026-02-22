package com.example.rental.user.adapter.outbound.persistence

import jakarta.persistence.*

/**
 * JPA entity — infrastructure concern only.
 * Maps to the "users" table.
 */
@Entity
@Table(name = "users")
data class UserJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String = "",
    val email: String = "",
    val password: String = ""
)
