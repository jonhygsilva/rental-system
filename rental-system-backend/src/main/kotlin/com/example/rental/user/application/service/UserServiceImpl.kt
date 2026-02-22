package com.example.rental.user.application.service

import com.example.rental.user.application.dto.*
import com.example.rental.user.application.port.input.CreateUserUseCase
import com.example.rental.user.application.port.input.LoginUseCase
import com.example.rental.user.application.port.output.JwtTokenPort
import com.example.rental.user.application.port.output.PasswordEncoderPort
import com.example.rental.user.application.port.output.UserPersistencePort
import com.example.rental.user.domain.exception.InvalidCredentialsException
import com.example.rental.user.domain.exception.UserAlreadyExistsException
import com.example.rental.user.domain.model.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Application service — orchestrates use cases using ports.
 * Contains NO infrastructure logic (no JPA, no HTTP).
 */
@Service
class UserServiceImpl(
    private val persistencePort: UserPersistencePort,
    private val passwordEncoderPort: PasswordEncoderPort,
    private val jwtTokenPort: JwtTokenPort
) : CreateUserUseCase, LoginUseCase {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    @Transactional
    override fun execute(command: CreateUserCommand): UserResponse {
        log.info("Creating user with email: {}", command.email)

        if (persistencePort.existsByEmail(command.email)) {
            log.warn("Attempt to create user with duplicate email: {}", command.email)
            throw UserAlreadyExistsException(command.email)
        }

        val encodedPassword = passwordEncoderPort.encode(command.password)

        val domainUser = User(
            name = command.name,
            email = command.email,
            password = encodedPassword
        )

        val saved = persistencePort.save(domainUser)
        log.info("User created successfully with id: {}", saved.id)
        return UserResponse.from(saved)
    }

    @Transactional(readOnly = true)
    override fun execute(request: LoginRequest): LoginResponse {
        log.info("Login attempt for email: {}", request.email)

        val user = persistencePort.findByEmail(request.email.trim().lowercase())
            ?: throw InvalidCredentialsException().also {
                log.warn("Login failed — email not found: {}", request.email)
            }

        if (!passwordEncoderPort.matches(request.password, user.password)) {
            log.warn("Login failed — wrong password for email: {}", request.email)
            throw InvalidCredentialsException()
        }

        val token = jwtTokenPort.generateToken(user.id, user.email)
        log.info("Login successful for user: {} (id: {})", user.email, user.id)

        return LoginResponse(
            token = token,
            userId = user.id,
            name = user.name,
            email = user.email
        )
    }
}
