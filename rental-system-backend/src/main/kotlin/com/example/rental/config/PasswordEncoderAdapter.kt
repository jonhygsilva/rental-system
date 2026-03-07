package com.example.rental.config

import com.example.rental.user.application.port.output.PasswordEncoderPort
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

/**
 * Infrastructure adapter — password encoding using BCrypt.
 */
@Component
class PasswordEncoderAdapter : PasswordEncoderPort {

    private val encoder = BCryptPasswordEncoder()

    override fun encode(rawPassword: String): String = encoder.encode(rawPassword)

    override fun matches(rawPassword: String, encodedPassword: String): Boolean =
        encoder.matches(rawPassword, encodedPassword)
}
