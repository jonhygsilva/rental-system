package com.example.rental.customer.domain.mapper

import com.example.rental.customer.infrastructure.persistence.entity.CustomerJpaEntity
import com.example.rental.customer.domain.model.Customer
import com.example.rental.customer.application.command.CreateCustomerCommand
import com.example.rental.customer.infrastructure.persistence.entity.AddressJpaEntity

fun CreateCustomerCommand.toDomain() = Customer(
    name = name,
    document = document,
    phone = phone,
    userId = userId,
    addresses = addresses.map { it.toDomain() }.toMutableList()
)

fun Customer.toJpaEntity(): CustomerJpaEntity {

    val customerJpa = CustomerJpaEntity(
        id = id,
        name = name,
        document = document,
        phone = phone,
        userId = userId
    )

    customerJpa.addresses.addAll(addresses.map { it.toJpaEntity(customerJpa) })

    return customerJpa
}

fun CustomerJpaEntity.toDomain(): Customer =
    Customer(
        id = id,
        name = name,
        document = document,
        phone = phone,
        userId = userId,
        addresses = addresses.map { it.toDomain() }.toMutableList()
    )