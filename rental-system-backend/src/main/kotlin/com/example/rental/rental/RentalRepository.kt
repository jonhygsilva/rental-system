package com.example.rental.rental

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface RentalRepository : JpaRepository<RentalEntity, Long> {
    fun findByUserId(userId: Long): List<RentalEntity>
    fun findByIdAndUserId(id: Long, userId: Long): RentalEntity?

    @Query("SELECT r.status, COUNT(r) FROM RentalEntity r WHERE r.userId = :userId GROUP BY r.status")
    fun countByStatusForUser(userId: Long): List<Array<Any>>

    fun countByUserIdAndStartDateBetween(userId: Long, start: LocalDate, end: LocalDate): Long

    @Query("SELECT COALESCE(SUM(r.total), 0) FROM RentalEntity r WHERE r.userId = :userId AND r.startDate BETWEEN :start AND :end")
    fun sumTotalByUserIdAndStartDateBetween(userId: Long, start: LocalDate, end: LocalDate): java.math.BigDecimal

    fun countByUserIdAndStatus(userId: Long, status: com.example.rental.rental.domain.model.RentalStatus): Long

    fun countByUserIdAndEndDateBeforeAndStatus(userId: Long, date: LocalDate, status: com.example.rental.rental.domain.model.RentalStatus): Long

    @Query("SELECT YEAR(r.startDate) as yr, MONTH(r.startDate) as mth, COALESCE(SUM(r.total), 0), COUNT(r) FROM RentalEntity r WHERE r.userId = :userId AND r.startDate BETWEEN :start AND :end GROUP BY YEAR(r.startDate), MONTH(r.startDate) ORDER BY YEAR(r.startDate), MONTH(r.startDate)")
    fun monthlyRevenueByUserBetween(userId: Long, start: LocalDate, end: LocalDate): List<Array<Any>>

    fun findByUserIdOrderByStartDateDesc(userId: Long, pageable: Pageable): List<RentalEntity>

    @Query("SELECT r.customer.id, r.customer.name, COUNT(r), COALESCE(SUM(r.total), 0) FROM RentalEntity r WHERE r.userId = :userId GROUP BY r.customer.id, r.customer.name ORDER BY COUNT(r) DESC")
    fun topCustomersByUser(userId: Long, pageable: Pageable): List<Array<Any>>
}
