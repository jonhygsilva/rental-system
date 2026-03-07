package com.example.rental.dashboard.application.port.input

import com.example.rental.dashboard.domain.model.DashboardTopCustomer

interface GetTopCustomersInput {
    fun getTopCustomers(userId: Long, size: Int): List<DashboardTopCustomer>
}
