package com.example.rental.customer.adapter.outbound.persistence.mapper

import com.example.rental.customer.adapter.outbound.persistence.entities.AddressJpaEntity
import com.example.rental.customer.domain.model.Address

/**
 * Extension functions to map between domain Address and JPA entity.
 */
fun Address.toJpaEntity() = AddressJpaEntity(
    id = id,
    street = street,
    number = number,
    complement = complement,
    neighborhood = neighborhood,
    city = city,
    state = state,
    zipCode = zipCode,
    customerId = customerId
)

fun AddressJpaEntity.toDomain() = Address(
    id = id,
    street = street,
    number = number,
    complement = complement,
    neighborhood = neighborhood,
    city = city,
    state = state,
    zipCode = zipCode,
    customerId = customerId
)

