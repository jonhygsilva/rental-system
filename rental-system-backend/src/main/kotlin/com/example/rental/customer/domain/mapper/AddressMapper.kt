package com.example.rental.customer.domain.mapper

import com.example.rental.customer.infrastructure.persistence.entity.AddressJpaEntity
import com.example.rental.customer.domain.model.Address
import com.example.rental.customer.application.command.CreateAddressCommand
import com.example.rental.customer.infrastructure.persistence.entity.CustomerJpaEntity

fun AddressJpaEntity.toDomain() = Address(
    id = id,
    street = street,
    number = number,
    complement = complement,
    neighborhood = neighborhood,
    city = city,
    state = state,
    zipCode = zipCode,
)

fun CreateAddressCommand.toDomain() = Address(
    street = street,
    number = number,
    complement = complement,
    neighborhood = neighborhood,
    city = city,
    state = state,
    zipCode = zipCode,
)

fun Address.toJpaEntity(customerJpa: CustomerJpaEntity) = AddressJpaEntity(
    id = id,
    street = street,
    number = number,
    complement = complement,
    neighborhood = neighborhood,
    city = city,
    state = state,
    zipCode = zipCode,
    customer = customerJpa
)
