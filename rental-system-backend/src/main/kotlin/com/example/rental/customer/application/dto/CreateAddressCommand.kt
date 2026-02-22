package com.example.rental.customer.application.dto

/**
 * Internal command — framework-agnostic representation of the create-address intent.
 */
data class CreateAddressCommand(
    val street: String,
    val number: String,
    val complement: String? = null,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val customerId: Long
)

