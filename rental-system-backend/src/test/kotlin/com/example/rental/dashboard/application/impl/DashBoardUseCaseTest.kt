package com.example.rental.dashboard.application.impl

import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.dashboard.domain.exception.DashboardDataAccessException
import com.example.rental.equipment.application.port.output.EquipmentPersistencePort
import com.example.rental.rental.application.port.output.RentalPersistencePort
import com.example.rental.rental.domain.model.RentalStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class DashBoardUseCaseTest {

    private lateinit var customerPersistencePort: CustomerPersistencePort
    private lateinit var rentalPersistencePort: RentalPersistencePort
    private lateinit var equipmentPersistencePort: EquipmentPersistencePort

    private lateinit var useCase: DashBoardUseCase

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        customerPersistencePort = Mockito.mock(CustomerPersistencePort::class.java)
        rentalPersistencePort = Mockito.mock(RentalPersistencePort::class.java)
        equipmentPersistencePort = Mockito.mock(EquipmentPersistencePort::class.java)

        useCase = DashBoardUseCase(customerPersistencePort, rentalPersistencePort, equipmentPersistencePort)
    }

    @Test
    fun `getDashboardSummary should return correct aggregated values`() {
        val userId = 1L
        val start = LocalDateTime.now().minusDays(7)
        val end = LocalDateTime.now()
        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()

        Mockito.`when`(rentalPersistencePort.sumTotalByUserIdAndStartDateBetween(userId, startDate, endDate))
            .thenReturn(BigDecimal.valueOf(1000L))
        Mockito.`when`(rentalPersistencePort.countByUserIdAndStartDateBetween(userId, startDate, endDate))
            .thenReturn(5L)
        // stub active rentals for exact status
        Mockito.`when`(rentalPersistencePort.countByUserIdAndStatus(userId, RentalStatus.ACTIVE))
            .thenReturn(2L)
        // For overdue count, stub using today's date and ACTIVE status
        val today = LocalDate.now()
        Mockito.`when`(rentalPersistencePort.countByUserIdAndEndDateBeforeAndStatus(userId, today, RentalStatus.ACTIVE))
            .thenReturn(1L)

        Mockito.`when`(customerPersistencePort.countByUserId(userId)).thenReturn(10L)
        Mockito.`when`(equipmentPersistencePort.countByUserId(userId)).thenReturn(20L)

        val summary = useCase.getDashboardSummary(userId, start, end)

        assertEquals(BigDecimal.valueOf(1000L), summary.totalRevenue)
        assertEquals(5L, summary.totalRentals)
        // active rentals value comes from mocked countByUserIdAndStatus; above we returned 2L
        assertEquals(2L, summary.activeRentals)
        assertEquals(1L, summary.overdueRentals)
        assertEquals(10L, summary.totalCustomers)
        assertEquals(20L, summary.totalEquipments)
    }

    @Test
    fun `getMonthlyRevenue should wrap exceptions into DashboardDataAccessException`() {
        val userId = 1L
        val start = LocalDateTime.now().minusMonths(3)
        val end = LocalDateTime.now()

        val startDate = start.toLocalDate()
        val endDate = end.toLocalDate()

        Mockito.`when`(rentalPersistencePort.getMonthlyRevenue(userId, startDate, endDate))
            .thenThrow(RuntimeException("db error"))

        assertThrows<DashboardDataAccessException> {
            useCase.getMonthlyRevenue(userId, start, end)
        }
    }

    @Test
    fun `getRecentRentals should wrap exceptions into DashboardDataAccessException`() {
        val userId = 1L
        Mockito.`when`(rentalPersistencePort.findRecentByUserId(userId, 5)).thenThrow(RuntimeException("db"))

        assertThrows<DashboardDataAccessException> {
            useCase.getRecentRentals(userId, 5)
        }
    }

    @Test
    fun `getTopCustomers should wrap exceptions into DashboardDataAccessException`() {
        val userId = 1L
        Mockito.`when`(rentalPersistencePort.findTopCustomersByUserId(userId, 3)).thenThrow(RuntimeException("db"))

        assertThrows<DashboardDataAccessException> {
            useCase.getTopCustomers(userId, 3)
        }
    }
}
