package com.example.rental.rental.application.port.output

import com.example.rental.dashboard.domain.model.DashboardMonthlyData
import com.example.rental.dashboard.domain.model.DashboardRecentRental
import com.example.rental.dashboard.domain.model.DashboardTopCustomer
import com.example.rental.rental.domain.model.Rental
import com.example.rental.rental.domain.model.RentalStatus
import java.math.BigDecimal
import java.time.LocalDate

interface RentalPersistencePort {
    fun save(rental: Rental): Rental
    fun findByUserId(userId: Long): List<Rental>
    fun findByIdAndUserId(id: Long, userId: Long): Rental?

    // returns a map of rental status -> count for a given user
    fun countByUserIdGroupByStatus(userId: Long): Map<RentalStatus, Long>

    fun sumTotalByUserIdAndStartDateBetween(userId: Long, start: LocalDate, end: LocalDate): BigDecimal

    // total rentals in period
    fun countByUserIdAndStartDateBetween(userId: Long, start: LocalDate, end: LocalDate): Long

    // count by status
    fun countByUserIdAndStatus(userId: Long, status: RentalStatus): Long

    // count overdue (endDate before date and specific status)
    fun countByUserIdAndEndDateBeforeAndStatus(userId: Long, date: LocalDate, status: RentalStatus): Long

    // monthly revenue and counts between start and end (month formatted as YYYY-MM)
    fun getMonthlyRevenue(userId: Long, start: LocalDate, end: LocalDate): List<DashboardMonthlyData>

    // recent rentals (ordered by startDate desc)
    fun findRecentByUserId(userId: Long, size: Int): List<DashboardRecentRental>

    // top customers by rentals for a given user
    fun findTopCustomersByUserId(userId: Long, size: Int): List<DashboardTopCustomer>
}
