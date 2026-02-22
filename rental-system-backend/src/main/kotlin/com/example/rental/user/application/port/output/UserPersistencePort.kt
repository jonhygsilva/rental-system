package com.example.rental.user.application.port.output

import com.example.rental.user.domain.model.User

/**
 * Output port — persistence operations for User.
 * Implemented by the infrastructure adapter (JPA, Mongo, etc.).
 */
interface UserPersistencePort {
    fun save(user: User): User
    fun findAll(): List<User>
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}

