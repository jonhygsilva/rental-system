package com.example.rental.user.application.port.input

import com.example.rental.user.web.dto.LoginRequest
import com.example.rental.user.web.dto.LoginResponse

/**
 * Input port — authenticate user and return JWT token.
 */
fun interface LoginInput {
    fun execute(request: LoginRequest): LoginResponse
}

