package com.example.rental.customer.application.port.input

import com.example.rental.customer.application.dto.CustomerResponse

/**
 * Input port — list customers by user.
 */
fun interface ListCustomersByUserUseCase {
    fun listByUser(userId: Long): List<CustomerResponse>
}
