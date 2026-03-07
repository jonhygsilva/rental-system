package com.example.rental.dashboard.application.port.input

import com.example.rental.dashboard.domain.model.DashboardMonthlyData
import java.time.LocalDateTime

interface GetMonthlyRevenueInput {
    fun getMonthlyRevenue(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<DashboardMonthlyData>
}
