package com.example.rental.user.infrastructure.persistence.adapter

import com.example.rental.user.infrastructure.persistence.repository.JpaUserRepository
import com.example.rental.user.domain.mapper.toDomain
import com.example.rental.user.domain.mapper.toJpaEntity
import com.example.rental.user.application.port.output.UserPersistencePort
import com.example.rental.user.domain.model.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Adapter that implements the output port using Spring Data JPA.
 * This is the only class that touches JPA — easy to swap for another implementation.
 */
@Component
class UserPersistenceAdapter(
    private val jpaRepository: JpaUserRepository
) : UserPersistencePort {

    private val log = LoggerFactory.getLogger(UserPersistenceAdapter::class.java)

    override fun save(user: User): User {
        log.debug("Persisting user: {}", user.email)
        val saved = jpaRepository.save(user.toJpaEntity())
        return saved.toDomain()
    }

    override fun findAll(): List<User> {
        log.debug("Finding all users")
        return jpaRepository.findAll().map { it.toDomain() }
    }

    override fun findById(id: Long): User? {
        log.debug("Finding user by id: {}", id)
        return jpaRepository.findById(id).orElse(null)?.toDomain()
    }

    override fun findByEmail(email: String): User? {
        log.debug("Finding user by email: {}", email)
        return jpaRepository.findByEmail(email)?.toDomain()
    }

    override fun existsByEmail(email: String): Boolean {
        return jpaRepository.existsByEmail(email)
    }
}