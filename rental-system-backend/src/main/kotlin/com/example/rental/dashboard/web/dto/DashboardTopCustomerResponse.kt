package com.example.rental.dashboard.web.dto

import com.example.rental.dashboard.domain.model.DashboardTopCustomer
import java.math.BigDecimal

data class DashboardTopCustomerResponse(
    val id: Long,
    val name: String,
    val totalRentals: Long,
    val totalSpent: BigDecimal
) {
    companion object {
        fun from(domain: DashboardTopCustomer) = DashboardTopCustomerResponse(
            id = domain.id,
            name = domain.name,
            totalRentals = domain.totalRentals,
            totalSpent = domain.totalSpent
        )
    }
}
