package com.example.rental.dashboard.domain.model

import java.math.BigDecimal

data class DashboardMonthlyData(
    val month: String, // format YYYY-MM
    val revenue: BigDecimal,
    val count: Long
)
