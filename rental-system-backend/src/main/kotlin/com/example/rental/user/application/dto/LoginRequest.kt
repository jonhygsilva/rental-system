package com.example.rental.user.application.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * DTO for login request.
 */
data class LoginRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)
