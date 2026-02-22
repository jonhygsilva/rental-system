package com.example.rental.user.application.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * DTO received from the REST layer.
 */
data class CreateUserRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters")
    val password: String
) {
    fun toCommand() = CreateUserCommand(
        name = name.trim(),
        email = email.trim().lowercase(),
        password = password
    )
}
