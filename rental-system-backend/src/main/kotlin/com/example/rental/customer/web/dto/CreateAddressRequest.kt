package com.example.rental.customer.web.dto

import jakarta.validation.constraints.NotBlank

data class CreateAddressRequest(
    @field:NotBlank(message = "Street is required")
    val street: String,

    @field:NotBlank(message = "Number is required")
    val number: String,

    val complement: String? = null,

    @field:NotBlank(message = "Neighborhood is required")
    val neighborhood: String,

    @field:NotBlank(message = "City is required")
    val city: String,

    @field:NotBlank(message = "State is required")
    val state: String,

    @field:NotBlank(message = "Zip code is required")
    val zipCode: String
)

