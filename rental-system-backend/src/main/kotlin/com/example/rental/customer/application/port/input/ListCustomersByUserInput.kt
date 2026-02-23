package com.example.rental.customer.application.port.input

import com.example.rental.customer.web.dto.CustomerResponse

/**
 * Input port — list customers by user.
 */
fun interface ListCustomersByUserInput {
    fun listByUser(userId: Long): List<CustomerResponse>
}
