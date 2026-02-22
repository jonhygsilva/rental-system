package com.example.rental.customer.domain.model

/**
 * Pure domain entity — represents a customer address.
 * A customer can have multiple addresses.
 */
data class Address(
    val id: Long = 0,
    val street: String,
    val number: String,
    val complement: String? = null,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val customerId: Long
) {
    init {
        require(street.isNotBlank()) { "Street must not be blank" }
        require(number.isNotBlank()) { "Number must not be blank" }
        require(neighborhood.isNotBlank()) { "Neighborhood must not be blank" }
        require(city.isNotBlank()) { "City must not be blank" }
        require(state.isNotBlank()) { "State must not be blank" }
        require(zipCode.isNotBlank()) { "Zip code must not be blank" }
        require(customerId > 0) { "customerId must be a positive number" }
    }
}

