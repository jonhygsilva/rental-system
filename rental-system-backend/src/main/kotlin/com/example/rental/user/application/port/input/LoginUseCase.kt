package com.example.rental.user.application.port.input

import com.example.rental.user.application.dto.LoginRequest
import com.example.rental.user.application.dto.LoginResponse

/**
 * Input port — authenticate user and return JWT token.
 */
fun interface LoginUseCase {
    fun execute(request: LoginRequest): LoginResponse
}

