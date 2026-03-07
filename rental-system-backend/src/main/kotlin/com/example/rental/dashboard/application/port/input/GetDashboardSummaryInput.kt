package com.example.rental.dashboard.application.port.input

import com.example.rental.dashboard.domain.model.DashboardSummary
import java.time.LocalDateTime

interface GetDashboardSummaryInput {
    fun getDashboardSummary(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime): DashboardSummary
}
