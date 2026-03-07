package com.example.rental.customer.web.dto

import com.example.rental.customer.domain.model.Customer
import java.time.LocalDate

/**
 * DTO returned to the REST layer.
 */
data class CustomerResponse(
    val id: Long,
    val name: String,
    val document: String,
    val phone: String,
    val createdAt: LocalDate,
    val addresses: List<AddressResponse> = emptyList()
) {
    companion object {
        fun from(customer: Customer) = CustomerResponse(
            id = customer.id ?: 0L,
            name = customer.name,
            document = customer.document,
            phone = customer.phone,
            createdAt = customer.createdAt,
            addresses = customer.addresses.map { AddressResponse.from(it) }
        )
    }
}
