package com.example.rental.dashboard.domain.model

import java.math.BigDecimal

data class DashboardTopCustomer(
    val id: Long,
    val name: String,
    val totalRentals: Long,
    val totalSpent: BigDecimal
)
