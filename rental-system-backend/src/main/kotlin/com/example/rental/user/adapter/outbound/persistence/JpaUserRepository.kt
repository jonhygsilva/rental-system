package com.example.rental.user.adapter.outbound.persistence

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository — infrastructure concern.
 */
interface JpaUserRepository : JpaRepository<UserJpaEntity, Long> {
    fun findByEmail(email: String): UserJpaEntity?
    fun existsByEmail(email: String): Boolean
}

