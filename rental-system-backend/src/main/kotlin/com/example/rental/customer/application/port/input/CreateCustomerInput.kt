package com.example.rental.customer.application.port.input

import com.example.rental.customer.application.command.CreateCustomerCommand
import com.example.rental.customer.web.dto.CustomerResponse

/**
 * Input port — create a new customer.
 */
fun interface CreateCustomerInput {
    fun execute(command: CreateCustomerCommand): CustomerResponse
}
