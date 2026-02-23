package com.example.rental.customer.application.port.output

import com.example.rental.customer.domain.model.Customer

interface CustomerPersistencePort {
    fun save(customer: Customer): Customer
    fun findById(id: Long): Customer?
    fun findByUserId(userId: Long): List<Customer>
    fun existsByDocument(document: String): Boolean
    fun deleteById(id: Long)
}