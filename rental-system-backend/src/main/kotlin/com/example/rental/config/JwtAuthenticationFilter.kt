package com.example.rental.config

import com.example.rental.user.application.port.output.JwtTokenPort
import com.example.rental.user.infrastructure.persistence.repository.JpaUserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * JWT authentication filter — intercepts every request and validates the Bearer token.
 */
@Component
class JwtAuthenticationFilter(
    private val jwtTokenPort: JwtTokenPort,
    private val userRepository: JpaUserRepository
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)

        if (jwtTokenPort.isTokenValid(token)) {
            val userId = jwtTokenPort.extractUserId(token)

            if (userId != null && SecurityContextHolder.getContext().authentication == null) {
                val userEntity = userRepository.findById(userId).orElse(null)

                if (userEntity != null) {
                    val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                    val authToken = UsernamePasswordAuthenticationToken(userId, null, authorities)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken

                    log.debug("Authenticated user: {}", userId)
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}
