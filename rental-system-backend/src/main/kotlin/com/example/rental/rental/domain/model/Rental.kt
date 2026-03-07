package com.example.rental.rental.domain.model

import com.example.rental.customer.domain.model.Address
import com.example.rental.customer.domain.model.Customer
import com.example.rental.equipment.domain.model.Equipment
import com.example.rental.rental.application.command.CreateRentalCommand
import java.math.BigDecimal
import java.time.LocalDate

data class Rental(
    val id: Long = 0,
    var customer: Customer,
    var equipment: Equipment,
    var address: Address,
    var userId: Long,
    var startDate: LocalDate,
    var endDate: LocalDate,
    var total: BigDecimal,
    var paid: Boolean = false,
    val status: RentalStatus
) {
    init {
        require(userId > 0) { "userId must be a positive number" }
        require(total >= BigDecimal.ZERO) { "total must be non-negative" }
    }

    fun updateFrom(src: CreateRentalCommand, customer: Customer, equipment: Equipment, address: Address) {
        this.customer = customer
        this.equipment = equipment
        this.address = address
        this.startDate = src.startDate
        this.endDate = src.endDate
        this.total = src.total
        this.paid = src.paid
    }

    fun withStatus(newStatus: RentalStatus): Rental = copy(status = newStatus)
}
