package com.example.rental.customer.application.port.input

import com.example.rental.common.web.dto.PaginatedResponse
import com.example.rental.customer.web.dto.CustomerResponse

interface GetCustomersInput {
    fun getCustomer(userId: Long, id: Long): CustomerResponse
    fun getCustomers(
        userId: Long,
        page: Int,
        size: Int,
        sort: String,
        search: String?,
        filters: Map<String, String>
    ): PaginatedResponse<CustomerResponse>
}
