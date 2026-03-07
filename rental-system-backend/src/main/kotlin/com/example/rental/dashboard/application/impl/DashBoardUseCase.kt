package com.example.rental.dashboard.application.impl

import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.dashboard.application.port.input.GetDashboardSummaryInput
import com.example.rental.dashboard.application.port.input.GetEquipmentStatsInput
import com.example.rental.dashboard.application.port.input.GetMonthlyRevenueInput
import com.example.rental.dashboard.application.port.input.GetRecentRentalsInput
import com.example.rental.dashboard.application.port.input.GetTopCustomersInput
import com.example.rental.dashboard.domain.exception.DashboardDataAccessException
import com.example.rental.dashboard.domain.exception.DashboardProcessingException
import com.example.rental.dashboard.domain.model.DashboardMonthlyData
import com.example.rental.dashboard.domain.model.DashboardRecentRental
import com.example.rental.dashboard.domain.model.DashboardSummary
import com.example.rental.dashboard.domain.model.DashboardTopCustomer
import com.example.rental.equipment.application.port.output.EquipmentPersistencePort
import com.example.rental.equipment.domain.model.EquipmentStatus
import com.example.rental.rental.application.port.output.RentalPersistencePort
import com.example.rental.rental.domain.model.RentalStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class DashBoardUseCase(
    private val customerPersistencePort: CustomerPersistencePort,
    private val rentalPersistencePort: RentalPersistencePort,
    private val equipmentPersistencePort: EquipmentPersistencePort
) : GetDashboardSummaryInput,
    GetEquipmentStatsInput,
    GetMonthlyRevenueInput,
    GetRecentRentalsInput,
    GetTopCustomersInput {

    private val log = LoggerFactory.getLogger(DashBoardUseCase::class.java)

    /** busca resumo para o dashboard, incluindo receita total, número de aluguéis, aluguéis ativos, aluguéis atrasados, número de clientes e equipamentos */
    override fun getDashboardSummary(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime): DashboardSummary {
        log.info("Building dashboard summary for userId={}, start={}, end={}", userId, startDate, endDate)
        return try {
            val startDateOnly: LocalDate = startDate.toLocalDate()
            val endDateOnly: LocalDate = endDate.toLocalDate()

            val totalRevenue = rentalPersistencePort.sumTotalByUserIdAndStartDateBetween(userId, startDateOnly, endDateOnly)
            val totalRentals = rentalPersistencePort.countByUserIdAndStartDateBetween(userId, startDateOnly, endDateOnly)
            val activeRentals = rentalPersistencePort.countByUserIdAndStatus(userId, RentalStatus.ACTIVE)
            val overdueRentals = rentalPersistencePort.countByUserIdAndEndDateBeforeAndStatus(userId, LocalDate.now(), RentalStatus.ACTIVE)

            val totalCustomers = customerPersistencePort.countByUserId(userId)
            val totalEquipments = equipmentPersistencePort.countByUserId(userId)

            val summary = DashboardSummary(
                totalRevenue = totalRevenue,
                totalRentals = totalRentals,
                activeRentals = activeRentals,
                overdueRentals = overdueRentals,
                totalCustomers = totalCustomers,
                totalEquipments = totalEquipments
            )

            log.debug("Dashboard summary built: {}", summary)
            summary
        } catch (ex: Exception) {
            log.error("Error building dashboard summary for userId={}", userId, ex)
            throw DashboardProcessingException("Failed to build dashboard summary for user: $userId", ex)
        }
    }

    /** busca estatísticas de equipamentos agrupados por status para o dashboard */
    @Transactional(readOnly = true)
    override fun getEquipmentStats(userId: Long): Map<EquipmentStatus, Long> {
        log.info("Fetching equipment stats for userId={}", userId)
        return try {
            equipmentPersistencePort.countByUserIdGroupByStatus(userId)
        } catch (ex: Exception) {
            log.error("Error fetching equipment stats for userId={}", userId, ex)
            throw DashboardDataAccessException("Failed to fetch equipment stats for user: $userId", ex)
        }
    }

    override fun getMonthlyRevenue(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime): List<DashboardMonthlyData> {
        log.info("Fetching monthly revenue for userId={}, start={}, end={}", userId, startDate, endDate)
        return try {
            val start = startDate.toLocalDate()
            val end = endDate.toLocalDate()
            val result = rentalPersistencePort.getMonthlyRevenue(userId, start, end)
            log.debug("Monthly revenue result size={} for userId={}", result.size, userId)
            result
        } catch (ex: Exception) {
            log.error("Error fetching monthly revenue for userId={}", userId, ex)
            throw DashboardDataAccessException("Failed to fetch monthly revenue for user: $userId", ex)
        }
    }

    override fun getRecentRentals(userId: Long, size: Int): List<DashboardRecentRental> {
        log.info("Fetching recent rentals for userId={}, size={}", userId, size)
        return try {
            val result = rentalPersistencePort.findRecentByUserId(userId, size)
            log.debug("Found {} recent rentals for userId={}", result.size, userId)
            result
        } catch (ex: Exception) {
            log.error("Error fetching recent rentals for userId={}", userId, ex)
            throw DashboardDataAccessException("Failed to fetch recent rentals for user: $userId", ex)
        }
    }

    override fun getTopCustomers(userId: Long, size: Int): List<DashboardTopCustomer> {
        log.info("Fetching top customers for userId={}, size={}", userId, size)
        return try {
            val result = rentalPersistencePort.findTopCustomersByUserId(userId, size)
            log.debug("Found {} top customers for userId={}", result.size, userId)
            result
        } catch (ex: Exception) {
            log.error("Error fetching top customers for userId={}", userId, ex)
            throw DashboardDataAccessException("Failed to fetch top customers for user: $userId", ex)
        }
    }
}
