package com.example.rental.user.adapter.outbound.persistence

import com.example.rental.user.domain.model.User

/**
 * Extension functions to map between domain model and JPA entity.
 */
fun User.toJpaEntity() = UserJpaEntity(
    id = id,
    name = name,
    email = email,
    password = password
)

fun UserJpaEntity.toDomain() = User(
    id = id,
    name = name,
    email = email,
    password = password
)
