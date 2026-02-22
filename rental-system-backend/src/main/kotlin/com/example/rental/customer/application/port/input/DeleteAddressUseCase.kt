package com.example.rental.customer.application.port.input

/**
 * Input port — delete an address.
 */
fun interface DeleteAddressUseCase {
    fun deleteAddress(addressId: Long)
}

