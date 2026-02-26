package com.example.rental.user.domain.mapper

import com.example.rental.user.application.command.CreateUserCommand
import com.example.rental.user.domain.model.User
import com.example.rental.user.infrastructure.persistence.entity.UserJpaEntity

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

fun CreateUserCommand.toDomain(encodedPassword: String) = User(
    name = name,
    email = email,
    password = encodedPassword
)