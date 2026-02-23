package com.example.rental.customer.application.port.input

import com.example.rental.customer.web.dto.CustomerResponse

/**
 * Input port — find customer by id.
 */
fun interface GetCustomerByIdInput {
    fun getById(id: Long): CustomerResponse
}
