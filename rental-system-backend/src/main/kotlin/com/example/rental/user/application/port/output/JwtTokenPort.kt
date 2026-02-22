package com.example.rental.user.application.port.output

/**
 * Output port — JWT token operations.
 */
interface JwtTokenPort {
    fun generateToken(userId: Long, email: String): String
    fun extractEmail(token: String): String?
    fun isTokenValid(token: String): Boolean
}
