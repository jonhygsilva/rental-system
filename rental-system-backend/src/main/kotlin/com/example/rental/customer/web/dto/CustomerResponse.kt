package com.example.rental.customer.web.dto

import com.example.rental.customer.domain.model.Customer

/**
 * DTO returned to the REST layer.
 */
data class CustomerResponse(
    val id: Long,
    val name: String,
    val document: String,
    val phone: String,
    val userId: Long,
    val addresses: List<AddressResponse> = emptyList()
) {
    companion object {
        fun from(customer: Customer) = CustomerResponse(
            id = customer.id ?: 0L,
            name = customer.name,
            document = customer.document,
            phone = customer.phone,
            userId = customer.userId,
            addresses = customer.addresses.map { AddressResponse.from(it) }
        )
    }
}
