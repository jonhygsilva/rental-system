package com.example.rental.customer.web

import com.example.rental.customer.domain.exception.CustomerNotFoundException
import com.example.rental.customer.domain.exception.DuplicateDocumentException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Centralized exception handling for the Customer module.
 */
@RestControllerAdvice(basePackageClasses = [CustomerController::class])
class CustomerExceptionHandler {

    private val log = LoggerFactory.getLogger(CustomerExceptionHandler::class.java)

    data class ErrorResponse(
        val status: Int,
        val error: String,
        val message: String?,
        val details: List<String> = emptyList()
    )

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleNotFound(ex: CustomerNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("Customer not found: {}", ex.identifier)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                status = 404,
                error = "Not Found",
                message = ex.message
            )
        )
    }

    @ExceptionHandler(DuplicateDocumentException::class)
    fun handleConflict(ex: DuplicateDocumentException): ResponseEntity<ErrorResponse> {
        log.warn("Duplicate customer document: {}", ex.document)
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
        log.error("Unexpected error in Customer module", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(
                status = 500,
                error = "Internal Server Error",
                message = "An unexpected error occurred"
            )
        )
    }
}