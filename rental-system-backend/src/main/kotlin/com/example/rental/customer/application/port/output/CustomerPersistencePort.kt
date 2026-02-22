package com.example.rental.customer.application.port.output

import com.example.rental.customer.domain.model.Customer

/**
 * Output port — persistence operations for Customer.
 * Implemented by the infrastructure adapter.
 */
interface CustomerPersistencePort {
    fun save(customer: Customer): Customer
    fun findAll(): List<Customer>
    fun findById(id: Long): Customer?
    fun findByUserId(userId: Long): List<Customer>
    fun findByDocument(document: String): Customer?
    fun existsByDocument(document: String): Boolean
    fun deleteById(id: Long)
}

