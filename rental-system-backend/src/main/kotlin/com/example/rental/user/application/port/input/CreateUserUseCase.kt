package com.example.rental.user.application.port.input

import com.example.rental.user.application.dto.CreateUserCommand
import com.example.rental.user.application.dto.UserResponse

/**
 * Input port — create a new user.
 */
fun interface CreateUserUseCase {
    fun execute(command: CreateUserCommand): UserResponse
}
