package com.example.rental.rental.web.dto

import com.example.rental.customer.web.dto.AddressResponse
import com.example.rental.customer.web.dto.CustomerResponse
import com.example.rental.equipment.web.dto.EquipmentResponse
import com.example.rental.rental.domain.model.Rental
import com.example.rental.rental.domain.model.RentalStatus
import java.math.BigDecimal
import java.time.LocalDate

data class RentalResponse(
    val id: Long,
    val customer: CustomerResponse,
    val equipment: EquipmentResponse,
    val address: AddressResponse,
    val userId: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val total: BigDecimal,
    val paid: Boolean = false,
    val status: RentalStatus = RentalStatus.ACTIVE
) {
    companion object {
        fun from(rental: Rental) = RentalResponse(
            id = rental.id,
            customer = CustomerResponse.from(rental.customer),
            equipment = EquipmentResponse.from(rental.equipment),
            address = AddressResponse.from(rental.address),
            userId = rental.userId,
            startDate = rental.startDate,
            endDate = rental.endDate,
            total = rental.total,
            paid = rental.paid,
            status = rental.status
        )
    }
}
