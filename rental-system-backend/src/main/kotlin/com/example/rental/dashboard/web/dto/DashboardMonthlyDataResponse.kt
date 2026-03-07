package com.example.rental.dashboard.web.dto

import com.example.rental.dashboard.domain.model.DashboardMonthlyData
import java.math.BigDecimal

data class DashboardMonthlyDataResponse(
    val month: String,
    val revenue: BigDecimal,
    val count: Long
) {
    companion object {
        fun from(domain: DashboardMonthlyData) = DashboardMonthlyDataResponse(
            month = domain.month,
            revenue = domain.revenue,
            count = domain.count
        )
    }
}
