package com.example.rental.customer.application.command

data class CreateAddressCommand(
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String
)