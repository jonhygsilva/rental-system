package com.example.rental.user.infrastructure.persistence.repository

import com.example.rental.user.infrastructure.persistence.entity.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository — infrastructure concern.
 */
interface JpaUserRepository : JpaRepository<UserJpaEntity, Long> {
    fun findByEmail(email: String): UserJpaEntity?
    fun existsByEmail(email: String): Boolean
}