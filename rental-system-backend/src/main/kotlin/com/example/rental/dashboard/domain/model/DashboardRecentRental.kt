package com.example.rental.dashboard.domain.model

import java.math.BigDecimal

data class DashboardRecentRental(
    val id: Long,
    val customerId: Long,
    val customerName: String?,
    val equipmentId: Long,
    val equipmentName: String?,
    val startDate: String,
    val endDate: String,
    val total: BigDecimal,
    val status: String
)
