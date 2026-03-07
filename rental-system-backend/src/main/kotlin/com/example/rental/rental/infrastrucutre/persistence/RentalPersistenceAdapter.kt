package com.example.rental.rental.infrastrucutre.persistence

import com.example.rental.dashboard.domain.model.DashboardMonthlyData
import com.example.rental.dashboard.domain.model.DashboardRecentRental
import com.example.rental.dashboard.domain.model.DashboardTopCustomer
import com.example.rental.rental.RentalRepository
import com.example.rental.rental.application.port.output.RentalPersistencePort
import com.example.rental.rental.domain.mapper.toDomain
import com.example.rental.rental.domain.mapper.toJpaEntity
import com.example.rental.rental.domain.model.Rental
import com.example.rental.rental.domain.model.RentalStatus
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class RentalPersistenceAdapter(private val rentalRepository: RentalRepository) : RentalPersistencePort {
    override fun save(rental: Rental): Rental {
        val saved = rentalRepository.save(rental.toJpaEntity())
        return saved.toDomain()
    }

    override fun findByUserId(userId: Long): List<Rental> {
        return rentalRepository.findByUserId(userId).map { it.toDomain() }
    }

    override fun findByIdAndUserId(id: Long, userId: Long): Rental? {
        return rentalRepository.findByIdAndUserId(id, userId)?.toDomain()
    }

    override fun countByUserIdGroupByStatus(userId: Long): Map<RentalStatus, Long> {
        val rows = rentalRepository.countByStatusForUser(userId)
        val result = mutableMapOf<RentalStatus, Long>()
        rows.forEach { row ->
            if (row.size >= 2) {
                val statusName = (row[0] as? Enum<*>)?.name
                val count = (row[1] as? Number)?.toLong() ?: 0L
                if (statusName != null) {
                    val domainStatus = RentalStatus.valueOf(statusName)
                    result[domainStatus] = count
                }
            }
        }
        return result
    }

    override fun sumTotalByUserIdAndStartDateBetween(userId: Long, start: LocalDate, end: LocalDate): BigDecimal {
        return rentalRepository.sumTotalByUserIdAndStartDateBetween(userId, start, end)
    }

    override fun countByUserIdAndStartDateBetween(userId: Long, start: LocalDate, end: LocalDate): Long {
        return rentalRepository.countByUserIdAndStartDateBetween(userId, start, end)
    }

    override fun countByUserIdAndStatus(userId: Long, status: RentalStatus): Long {
        return rentalRepository.countByUserIdAndStatus(userId, status)
    }

    override fun countByUserIdAndEndDateBeforeAndStatus(userId: Long, date: LocalDate, status: RentalStatus): Long {
        return rentalRepository.countByUserIdAndEndDateBeforeAndStatus(userId, date, status)
    }

    override fun getMonthlyRevenue(userId: Long, start: LocalDate, end: LocalDate): List<DashboardMonthlyData> {
        val rows = rentalRepository.monthlyRevenueByUserBetween(userId, start, end)
        val result = mutableListOf<DashboardMonthlyData>()
        rows.forEach { row ->
            if (row.size >= 4) {
                val year = (row[0] as? Number)?.toInt() ?: 0
                val month = (row[1] as? Number)?.toInt() ?: 0
                val revenue = (row[2] as? BigDecimal) ?: BigDecimal.ZERO
                val count = (row[3] as? Number)?.toLong() ?: 0L
                val monthStr = String.format("%04d-%02d", year, month)
                result.add(DashboardMonthlyData(month = monthStr, revenue = revenue, count = count))
            }
        }
        return result
    }

    override fun findRecentByUserId(userId: Long, size: Int): List<DashboardRecentRental> {
        val page = PageRequest.of(0, size)
        val rows = rentalRepository.findByUserIdOrderByStartDateDesc(userId, page)
        val formatter = DateTimeFormatter.ISO_DATE
        return rows.map { r ->
            DashboardRecentRental(
                id = r.id,
                customerId = r.customer.id ?: 0L,
                customerName = r.customer.name,
                equipmentId = r.equipment.id,
                equipmentName = r.equipment.name,
                startDate = r.startDate.format(formatter),
                endDate = r.endDate.format(formatter),
                total = r.total,
                status = r.status.name
            )
        }
    }

    override fun findTopCustomersByUserId(userId: Long, size: Int): List<DashboardTopCustomer> {
        val page = PageRequest.of(0, size)
        val rows = rentalRepository.topCustomersByUser(userId, page)
        val result = mutableListOf<DashboardTopCustomer>()
        rows.forEach { row ->
            if (row.size >= 4) {
                val customerId = (row[0] as? Number)?.toLong() ?: 0L
                val name = (row[1] as? String) ?: ""
                val count = (row[2] as? Number)?.toLong() ?: 0L
                val sum = (row[3] as? BigDecimal) ?: BigDecimal.ZERO
                result.add(DashboardTopCustomer(id = customerId, name = name, totalRentals = count, totalSpent = sum))
            }
        }
        return result
    }
}
