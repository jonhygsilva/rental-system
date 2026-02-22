package com.example.rental.user.application.dto

/**
 * Internal command — framework-agnostic representation of the create-user intent.
 */
data class CreateUserCommand(
    val name: String,
    val email: String,
    val password: String
)
