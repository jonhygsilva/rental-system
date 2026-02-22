package com.example.rental.customer.application.dto

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
        fun from(customer: Customer, addresses: List<AddressResponse> = emptyList()) = CustomerResponse(
            id = customer.id,
            name = customer.name,
            document = customer.document,
            phone = customer.phone,
            userId = customer.userId,
            addresses = addresses
        )
    }
}
