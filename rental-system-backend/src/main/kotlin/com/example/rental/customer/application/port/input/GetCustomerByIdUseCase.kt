package com.example.rental.customer.application.port.input

import com.example.rental.customer.application.dto.CustomerResponse

/**
 * Input port — find customer by id.
 */
fun interface GetCustomerByIdUseCase {
    fun getById(id: Long): CustomerResponse
}
