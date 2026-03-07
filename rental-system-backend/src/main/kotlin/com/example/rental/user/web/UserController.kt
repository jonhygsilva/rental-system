package com.example.rental.user.web

import com.example.rental.user.application.port.input.CreateUserInput
import com.example.rental.user.application.port.input.LoginInput
import com.example.rental.user.web.dto.CreateUserRequest
import com.example.rental.user.web.dto.LoginRequest
import com.example.rental.user.web.dto.LoginResponse
import com.example.rental.user.web.dto.UserResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Inbound REST adapter — user registration and authentication.
 * Contains NO business logic.
 */
@RestController
@RequestMapping("/api/users")
class UserController(
    private val createUserInput: CreateUserInput,
    private val loginInput: LoginInput
) {

    private val log = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody
        request: CreateUserRequest
    ): ResponseEntity<UserResponse> {
        log.info("POST /api/users/register — email: {}", request.email)
        val response = createUserInput.execute(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody
        request: LoginRequest
    ): ResponseEntity<LoginResponse> {
        log.info("POST /api/users/login — email: {}", request.email)
        val response = loginInput.execute(request)
        return ResponseEntity.ok(response)
    }
}
