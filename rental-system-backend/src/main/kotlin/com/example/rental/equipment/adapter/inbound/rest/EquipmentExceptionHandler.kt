package com.example.rental.equipment.adapter.inbound.rest

import com.example.rental.equipment.domain.exception.EquipmentNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Centralized exception handling for the Equipment module.
 */
@RestControllerAdvice(basePackageClasses = [EquipmentController::class])
class EquipmentExceptionHandler {

    private val log = LoggerFactory.getLogger(EquipmentExceptionHandler::class.java)

    data class ErrorResponse(
        val status: Int,
        val error: String,
        val message: String?,
        val details: List<String> = emptyList()
    )

    @ExceptionHandler(EquipmentNotFoundException::class)
    fun handleNotFound(ex: EquipmentNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("Equipment not found: {}", ex.identifier)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(status = 404, error = "Not Found", message = ex.message)
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val details = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        log.warn("Validation failed: {}", details)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(status = 400, error = "Bad Request", message = "Validation failed", details = details)
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        log.warn("Invalid argument: {}", ex.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(status = 400, error = "Bad Request", message = ex.message)
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> {
        log.error("Unexpected error in Equipment module", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(status = 500, error = "Internal Server Error", message = "An unexpected error occurred")
        )
    }
}

