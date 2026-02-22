package com.example.rental.customer.adapter.outbound.persistence.mapper

import com.example.rental.customer.adapter.outbound.persistence.entities.CustomerJpaEntity
import com.example.rental.customer.domain.model.Customer

/**
 * Extension functions to map between domain model and JPA entity.
 */
fun Customer.toJpaEntity() = CustomerJpaEntity(
    id = id,
    name = name,
    document = document,
    phone = phone,
    userId = userId
)

fun CustomerJpaEntity.toDomain() = Customer(
    id = id,
    name = name,
    document = document,
    phone = phone,
    userId = userId
)

