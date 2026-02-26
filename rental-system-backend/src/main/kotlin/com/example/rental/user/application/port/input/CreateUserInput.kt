package com.example.rental.user.application.port.input

import com.example.rental.user.application.command.CreateUserCommand
import com.example.rental.user.web.dto.UserResponse

/**
 * Input port — create a new user.
 */
fun interface CreateUserInput {
    fun execute(command: CreateUserCommand): UserResponse
}
