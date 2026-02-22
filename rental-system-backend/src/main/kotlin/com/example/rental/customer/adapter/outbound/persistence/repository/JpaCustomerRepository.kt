package com.example.rental.customer.adapter.outbound.persistence.repository

import com.example.rental.customer.adapter.outbound.persistence.entities.CustomerJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository — infrastructure concern.
 */
interface JpaCustomerRepository : JpaRepository<CustomerJpaEntity, Long> {
    fun findByUserId(userId: Long): List<CustomerJpaEntity>
    fun findByDocument(document: String): CustomerJpaEntity?
    fun existsByDocument(document: String): Boolean
}