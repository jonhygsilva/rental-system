package com.example.rental.customer.application.port.input

import com.example.rental.customer.application.dto.CreateCustomerCommand
import com.example.rental.customer.application.dto.CustomerResponse

/**
 * Input port — create a new customer.
 */
fun interface CreateCustomerUseCase {
    fun execute(command: CreateCustomerCommand): CustomerResponse
}
