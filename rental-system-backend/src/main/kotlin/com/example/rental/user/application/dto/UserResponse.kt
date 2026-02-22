package com.example.rental.user.application.dto

import com.example.rental.user.domain.model.User

/**
 * DTO returned to the REST layer — never exposes the password.
 */
data class UserResponse(
    val id: Long,
    val name: String,
    val email: String
) {
    companion object {
        fun from(user: User) = UserResponse(
            id = user.id,
            name = user.name,
            email = user.email
        )
    }
}

