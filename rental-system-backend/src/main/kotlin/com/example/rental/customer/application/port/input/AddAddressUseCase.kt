package com.example.rental.customer.application.port.input

import com.example.rental.customer.application.dto.AddressResponse
import com.example.rental.customer.application.dto.CreateAddressCommand

/**
 * Input port — add address to a customer.
 */
fun interface AddAddressUseCase {
    fun addAddress(command: CreateAddressCommand): AddressResponse
}
