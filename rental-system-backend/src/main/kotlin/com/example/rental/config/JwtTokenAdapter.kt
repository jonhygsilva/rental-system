package com.example.rental.config

import com.example.rental.user.application.port.output.JwtTokenPort
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenAdapter(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration}") private val expirationMs: Long
) : JwtTokenPort {

    private val log = LoggerFactory.getLogger(JwtTokenAdapter::class.java)

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))
    }

    override fun generateToken(userId: Long, email: String): String {
        val now = Date()
        val expiry = Date(now.time + expirationMs)

        log.debug("Generating JWT for user: {} (id: {})", email, userId)

        return Jwts.builder()
            .subject(email)
            .claim("userId", userId)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(key)
            .compact()
    }

    override fun extractEmail(token: String): String? {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        } catch (ex: Exception) {
            log.warn("Failed to extract email from token: {}", ex.message)
            null
        }
    }

    override fun isTokenValid(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            true
        } catch (ex: Exception) {
            log.warn("Invalid JWT token: {}", ex.message)
            false
        }
    }
}
