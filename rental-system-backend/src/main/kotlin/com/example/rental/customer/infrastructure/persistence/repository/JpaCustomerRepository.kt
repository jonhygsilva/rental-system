package com.example.rental.customer.infrastructure.persistence.repository

import com.example.rental.customer.infrastructure.persistence.entity.CustomerJpaEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository — infrastructure concern.
 */
interface JpaCustomerRepository : JpaRepository<CustomerJpaEntity, Long> {
    @EntityGraph(attributePaths = ["addresses"])
    fun findByUserId(userId: Long): List<CustomerJpaEntity>
    fun existsByDocument(document: String): Boolean
}