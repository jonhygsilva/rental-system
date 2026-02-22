package com.example.rental.user.application.port.output

/**
 * Output port — password encoding operations.
 */
interface PasswordEncoderPort {
    fun encode(rawPassword: String): String
    fun matches(rawPassword: String, encodedPassword: String): Boolean
}

