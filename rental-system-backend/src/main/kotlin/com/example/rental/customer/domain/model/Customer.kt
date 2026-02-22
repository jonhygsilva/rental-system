package com.example.rental.customer.domain.model

/**
 * Pure domain entity — no framework annotations.
 * Represents a customer (client) of the rental system.
 */
data class Customer(
    val id: Long = 0,
    val name: String,
    val document: String,
    val phone: String,
    val userId: Long
) {
    init {
        require(name.isNotBlank()) { "Customer name must not be blank" }
        require(document.isNotBlank()) { "Document must not be blank" }
        require(phone.isNotBlank()) { "Phone must not be blank" }
        require(userId > 0) { "userId must be a positive number" }
    }
}
