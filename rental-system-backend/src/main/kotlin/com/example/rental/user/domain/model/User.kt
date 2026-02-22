package com.example.rental.user.domain.model

/**
 * Pure domain entity — no framework annotations.
 * Represents a system user with authentication data.
 */
data class User(
    val id: Long = 0,
    val name: String,
    val email: String,
    val password: String
) {
    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(email.isNotBlank()) { "Email must not be blank" }
        require(password.isNotBlank()) { "Password must not be blank" }
        require(EMAIL_REGEX.matches(email)) { "Invalid email format: $email" }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}
