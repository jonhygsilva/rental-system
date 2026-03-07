package com.example.rental.rental.application.command

import com.example.rental.rental.domain.model.RentalStatus
import java.math.BigDecimal
import java.time.LocalDate

data class CreateRentalCommand(
    val customerId: Long,
    val equipmentId: Long,
    val addressId: Long,
    val userId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val total: BigDecimal,
    val paid: Boolean = false,
    val status: RentalStatus = RentalStatus.ACTIVE
)
