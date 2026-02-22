package com.example.rental.user.adapter.inbound.rest

import com.example.rental.user.domain.exception.InvalidCredentialsException
import com.example.rental.user.domain.exception.UserAlreadyExistsException
import com.example.rental.user.domain.exception.UserNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Centralized exception handling for the User module.
 */
@RestControllerAdvice(basePackageClasses = [UserController::class])
class UserExceptionHandler {

    private val log = LoggerFactory.getLogger(UserExceptionHandler::class.java)

    data class ErrorResponse(
        val status: Int,
        val error: String,
        val message: String?,
        val details: List<String> = emptyList()
    )

    @ExceptionHandler(UserNotFoundException::class)
    fun handleNotFound(ex: UserNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("User not found: {}", ex.identifier)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                status = 404,
                error = "Not Found",
                message = ex.message
            )
        )
    }

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(ex: InvalidCredentialsException): ResponseEntity<ErrorResponse> {
        log.warn("Invalid credentials attempt")
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse(
                status = 401,
                error = "Unauthorized",
                message = ex.message
            )
        )
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleConflict(ex: UserAlreadyExistsException): ResponseEntity<ErrorResponse> {
        log.warn("Duplicate user email: {}", ex.email)
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse(
                status = 409,
                error = "Conflict",
                message = ex.message
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val details = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        log.warn("Validation failed: {}", details)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                status = 400,
                error = "Bad Request",
                message = "Validation failed",
                details = details
            )
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        log.warn("Invalid argument: {}", ex.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                status = 400,
                error = "Bad Request",
                message = ex.message
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> {
        log.error("Unexpected error in User module", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(
                status = 500,
                error = "Internal Server Error",
                message = "An unexpected error occurred"
            )
        )
    }
}

