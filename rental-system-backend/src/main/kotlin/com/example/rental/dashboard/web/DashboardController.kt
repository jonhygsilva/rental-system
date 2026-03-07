package com.example.rental.dashboard.web

import com.example.rental.dashboard.application.port.input.GetDashboardSummaryInput
import com.example.rental.dashboard.application.port.input.GetEquipmentStatsInput
import com.example.rental.dashboard.application.port.input.GetMonthlyRevenueInput
import com.example.rental.dashboard.application.port.input.GetRecentRentalsInput
import com.example.rental.dashboard.application.port.input.GetTopCustomersInput
import com.example.rental.dashboard.web.dto.DashboardMonthlyDataResponse
import com.example.rental.dashboard.web.dto.DashboardRecentRentalResponse
import com.example.rental.dashboard.web.dto.DashboardSummaryResponse
import com.example.rental.dashboard.web.dto.DashboardTopCustomerResponse
import com.example.rental.equipment.domain.model.EquipmentStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val dashboardSummaryInput: GetDashboardSummaryInput,
    private val equipmentStatsInput: GetEquipmentStatsInput,
    private val monthlyRevenueInput: GetMonthlyRevenueInput,
    private val recentRentalsInput: GetRecentRentalsInput,
    private val topCustomersInput: GetTopCustomersInput
) {

    @GetMapping("/summary")
    fun getSummary(
        @AuthenticationPrincipal userId: Long,
        @RequestParam(required = false) start: String?,
        @RequestParam(required = false) end: String?
    ): ResponseEntity<DashboardSummaryResponse> {
        val now = LocalDateTime.now()
        val startDt = parseDateParam(start) ?: now.minusDays(30)
        val endDt = parseDateParam(end) ?: now

        val summary = dashboardSummaryInput.getDashboardSummary(userId, startDt, endDt)
        return ResponseEntity.ok(DashboardSummaryResponse.from(summary))
    }

    @GetMapping("/equipment-stats")
    fun getEquipmentStats(@AuthenticationPrincipal userId: Long): ResponseEntity<Map<EquipmentStatus, Long>> {
        val stats = equipmentStatsInput.getEquipmentStats(userId)
        return ResponseEntity.ok(stats)
    }

    @GetMapping("/monthly-revenue")
    fun getMonthlyRevenue(
        @AuthenticationPrincipal userId: Long,
        @RequestParam(required = false) start: String?,
        @RequestParam(required = false) end: String?
    ): ResponseEntity<List<DashboardMonthlyDataResponse>> {
        val now = LocalDateTime.now()
        val startDt = parseDateParam(start) ?: now.minusMonths(6)
        val endDt = parseDateParam(end) ?: now

        val monthly = monthlyRevenueInput.getMonthlyRevenue(userId, startDt, endDt)
        return ResponseEntity.ok(monthly.map { DashboardMonthlyDataResponse.from(it) })
    }

    @GetMapping("/recent-rentals")
    fun getRecentRentals(
        @AuthenticationPrincipal userId: Long,
        @RequestParam(required = false, defaultValue = "5") size: Int
    ): ResponseEntity<List<DashboardRecentRentalResponse>> {
        val recent = recentRentalsInput.getRecentRentals(userId, size)
        return ResponseEntity.ok(recent.map { DashboardRecentRentalResponse.from(it) })
    }

    @GetMapping("/top-customers")
    fun getTopCustomers(
        @AuthenticationPrincipal userId: Long,
        @RequestParam(required = false, defaultValue = "5") size: Int
    ): ResponseEntity<List<DashboardTopCustomerResponse>> {
        val top = topCustomersInput.getTopCustomers(userId, size)
        return ResponseEntity.ok(top.map { DashboardTopCustomerResponse.from(it) })
    }

    private fun parseDateParam(value: String?): LocalDateTime? {
        if (value == null) return null
        return try {
            if (value.contains("T")) {
                LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME)
            } else {
                LocalDate.parse(value, DateTimeFormatter.ISO_DATE).atStartOfDay()
            }
        } catch (ex: Exception) {
            null
        }
    }
}
