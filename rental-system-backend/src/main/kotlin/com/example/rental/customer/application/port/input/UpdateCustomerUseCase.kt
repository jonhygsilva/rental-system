package com.example.rental.customer.application.port.input

import com.example.rental.customer.application.dto.CreateCustomerCommand
import com.example.rental.customer.application.dto.CustomerResponse

/**
 * Input port — update an existing customer with full payload similar to POST.
 */
fun interface UpdateCustomerUseCase {
    fun update(id: Long, command: CreateCustomerCommand): CustomerResponse
}

