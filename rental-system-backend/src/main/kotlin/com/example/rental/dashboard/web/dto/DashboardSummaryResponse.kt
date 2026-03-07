package com.example.rental.dashboard.web.dto

import com.example.rental.dashboard.domain.model.DashboardSummary
import java.math.BigDecimal

data class DashboardSummaryResponse(
    val totalRevenue: BigDecimal,
    val totalRentals: Long,
    val activeRentals: Long,
    val overdueRentals: Long,
    val totalCustomers: Long,
    val totalEquipments: Long
) {
    companion object {
        fun from(domain: DashboardSummary) = DashboardSummaryResponse(
            totalRevenue = domain.totalRevenue,
            totalRentals = domain.totalRentals,
            activeRentals = domain.activeRentals,
            overdueRentals = domain.overdueRentals,
            totalCustomers = domain.totalCustomers,
            totalEquipments = domain.totalEquipments
        )
    }
}
