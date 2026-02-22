package com.example.rental.customer.application.port.output

import com.example.rental.customer.domain.model.Address

/**
 * Output port — persistence operations for Address.
 * Implemented by the infrastructure adapter.
 */
interface AddressPersistencePort {
    fun save(address: Address): Address
    fun findById(id: Long): Address?
    fun findByCustomerId(customerId: Long): List<Address>
    fun deleteById(id: Long)
}

