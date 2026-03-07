package com.example.rental.user.web.dto

/**
 * DTO returned after successful login — contains the JWT token.
 */
data class LoginResponse(
    val token: String,
    val userId: Long,
    val name: String,
    val email: String
)
