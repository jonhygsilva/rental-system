package com.example.rental.rental.domain.mapper

import com.example.rental.customer.domain.mapper.toDomain
import com.example.rental.customer.domain.mapper.toJpaEntity
import com.example.rental.customer.domain.model.Address
import com.example.rental.customer.domain.model.Customer
import com.example.rental.equipment.domain.mapper.toDomain
import com.example.rental.equipment.domain.mapper.toJpaEntity
import com.example.rental.equipment.domain.model.Equipment
import com.example.rental.rental.RentalEntity
import com.example.rental.rental.application.command.CreateRentalCommand
import com.example.rental.rental.domain.model.Rental

fun CreateRentalCommand.toDomain(customer: Customer, equipment: Equipment, address: Address) = Rental(
    customer = customer,
    equipment = equipment,
    address = address,
    userId = this.userId,
    startDate = this.startDate,
    endDate = this.endDate,
    total = this.total,
    paid = this.paid,
    status = this.status
)

fun Rental.toJpaEntity(): RentalEntity {
    val customerEntity = customer.toJpaEntity()

    return RentalEntity(
        id = id,
        customer = customerEntity,
        equipment = equipment.toJpaEntity(),
        address = address.toJpaEntity(customerEntity),
        userId = userId,
        startDate = startDate,
        endDate = endDate,
        total = total,
        paid = paid,
        status = status
    )
}

fun RentalEntity.toDomain() = Rental(
    id = id,
    customer = customer.toDomain(),
    equipment = equipment.toDomain(),
    address = address.toDomain(),
    userId = userId,
    startDate = startDate,
    endDate = endDate,
    total = total,
    paid = paid,
    status = status
)
