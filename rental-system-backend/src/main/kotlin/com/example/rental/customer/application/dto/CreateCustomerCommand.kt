package com.example.rental.customer.application.dto

/**
 * Internal command — framework-agnostic representation of the create-customer intent.
 * Optionally includes addresses to persist in the same transaction.
 */
data class CreateCustomerCommand(
    val name: String,
    val document: String,
    val phone: String,
    val userId: Long,
    val addresses: List<CreateAddressCommand> = emptyList()
)
