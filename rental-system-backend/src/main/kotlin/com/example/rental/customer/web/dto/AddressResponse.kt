package com.example.rental.customer.web.dto

import com.example.rental.customer.domain.model.Address

/**
 * DTO returned to the REST layer.
 */
data class AddressResponse(
    val id: Long,
    val street: String,
    val number: String,
    val complement: String?,
    val neighborhood: String,
    val city: String,
    val state: String,
    val zipCode: String
) {
    companion object {
        fun from(address: Address) = AddressResponse(
            id = address.id ?: 0L,
            street = address.street,
            number = address.number,
            complement = address.complement,
            neighborhood = address.neighborhood,
            city = address.city,
            state = address.state,
            zipCode = address.zipCode
        )
    }
}

