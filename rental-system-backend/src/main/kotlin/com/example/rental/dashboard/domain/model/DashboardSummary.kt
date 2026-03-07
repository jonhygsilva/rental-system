package com.example.rental.dashboard.domain.model

import java.math.BigDecimal

data class DashboardSummary(
    val totalRevenue: BigDecimal,
    val totalRentals: Long,
    val activeRentals: Long,
    val overdueRentals: Long,
    val totalCustomers: Long,
    val totalEquipments: Long
)
