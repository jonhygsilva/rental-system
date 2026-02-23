package com.example.rental.customer.domain.model

data class Address(
    val id: Long? = null,
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String
) {
    init {
        require(street.isNotBlank()) { "Street must not be blank" }
        require(number.isNotBlank()) { "Number must not be blank" }
        require(neighborhood.isNotBlank()) { "Neighborhood must not be blank" }
        require(city.isNotBlank()) { "City must not be blank" }
        require(state.isNotBlank()) { "State must not be blank" }
        require(zipCode.isNotBlank()) { "Zip code must not be blank" }
    }
}
