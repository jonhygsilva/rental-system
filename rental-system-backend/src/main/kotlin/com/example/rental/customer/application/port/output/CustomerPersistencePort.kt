package com.example.rental.customer.application.port.output

import com.example.rental.customer.domain.model.Customer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface CustomerPersistencePort {
    fun save(customer: Customer): Customer
    fun findByUserId(userId: Long): List<Customer>
    fun findByIdAndUserId(id: Long, userId: Long): Customer?
    fun existsByDocumentAndUserId(document: String, userId: Long): Boolean
    fun deleteByIdAndUserId(id: Long, userId: Long)

    // count customers created between two timestamps for a specific user
    fun countByUserIdAndCreatedAtBetween(userId: Long, start: LocalDateTime, end: LocalDateTime): Long

    // total customers for a user
    fun countByUserId(userId: Long): Long

    // paginated search for customers with optional filters
    fun findByUserIdWithFilters(userId: Long, search: String?, document: String?, phone: String?, pageable: Pageable): Page<Customer>
}
