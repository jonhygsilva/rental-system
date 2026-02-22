package com.example.rental.customer.application.dto

import jakarta.validation.constraints.NotBlank

/**
 * DTO received from the REST layer to create an address.
 */
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
) {
    fun toCommand(customerId: Long) = CreateAddressCommand(
        street = street.trim(),
        number = number.trim(),
        complement = complement?.trim(),
        neighborhood = neighborhood.trim(),
        city = city.trim(),
        state = state.trim(),
        zipCode = zipCode.trim(),
        customerId = customerId
    )
}
