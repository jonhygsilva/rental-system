package com.example.rental.customer.application.port.input

import com.example.rental.customer.application.command.CreateCustomerCommand
import com.example.rental.customer.web.dto.CustomerResponse

/**
 * Input port — update an existing customer with full payload similar to POST.
 */
fun interface UpdateCustomerInput {
    fun update(userId: Long, id: Long, command: CreateCustomerCommand): CustomerResponse
}
