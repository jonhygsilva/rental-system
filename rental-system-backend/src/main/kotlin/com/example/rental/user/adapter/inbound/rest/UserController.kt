package com.example.rental.user.adapter.inbound.rest

import com.example.rental.user.application.dto.CreateUserRequest
import com.example.rental.user.application.dto.LoginRequest
import com.example.rental.user.application.dto.LoginResponse
import com.example.rental.user.application.dto.UserResponse
import com.example.rental.user.application.port.input.CreateUserUseCase
import com.example.rental.user.application.port.input.LoginUseCase
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Inbound REST adapter — user registration and authentication.
 * Contains NO business logic.
 */
@RestController
@RequestMapping("/api/users")
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val loginUseCase: LoginUseCase
) {

    private val log = LoggerFactory.getLogger(UserController::class.java)

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        log.info("POST /api/users/register — email: {}", request.email)
        val response = createUserUseCase.execute(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        log.info("POST /api/users/login — email: {}", request.email)
        val response = loginUseCase.execute(request)
        return ResponseEntity.ok(response)
    }
}
