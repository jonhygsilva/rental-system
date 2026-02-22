package com.example.rental.customer.application.port.input

import com.example.rental.customer.application.dto.AddressResponse

/**
 * Input port — list addresses of a customer.
 */
fun interface ListAddressesByCustomerUseCase {
    fun listByCustomer(customerId: Long): List<AddressResponse>
}

