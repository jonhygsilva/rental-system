package com.example.rental.customer.application.port.input

/**
 * Input port — delete a customer and all their addresses.
 */
fun interface DeleteCustomerUseCase {
    fun delete(id: Long)
}
