package com.example.rental.rental.web.dto

import com.example.rental.rental.application.command.CreateRentalCommand
import com.example.rental.rental.domain.model.RentalStatus
import java.math.BigDecimal
import java.time.LocalDate

data class CreateRentalRequest(
    val customerId: Long,
    val equipmentId: Long,
    val addressId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val total: BigDecimal,
    val paid: Boolean = false,
    val status: RentalStatus = RentalStatus.ACTIVE
) {
    fun toCommand(userId: Long) = CreateRentalCommand(
        customerId = customerId,
        equipmentId = equipmentId,
        addressId = addressId,
        userId = userId,
        startDate = startDate,
        endDate = endDate,
        total = total,
        paid = paid,
        status = status
    )
}
