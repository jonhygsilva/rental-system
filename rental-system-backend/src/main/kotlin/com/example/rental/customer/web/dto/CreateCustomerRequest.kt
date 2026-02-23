package com.example.rental.customer.web.dto

import com.example.rental.customer.application.command.CreateAddressCommand
import com.example.rental.customer.application.command.CreateCustomerCommand
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

/**
 * DTO received from the REST layer.
 * Optionally includes addresses to create together with the customer.
 */
data class CreateCustomerRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Document is required")
    val document: String,

    @field:NotBlank(message = "Phone is required")
    val phone: String,

    @field:Positive(message = "userId must be a positive number")
    val userId: Long,

    @field:Valid
    val addresses: List<CreateAddressRequest> = emptyList()
) {
    fun toCommand() = CreateCustomerCommand(
        name = name.trim(),
        document = document.trim(),
        phone = phone.trim(),
        userId = userId,
        addresses = addresses.map {
            CreateAddressCommand(
                street = it.street.trim(),
                number = it.number.trim(),
                complement = it.complement?.trim(),
                neighborhood = it.neighborhood.trim(),
                city = it.city.trim(),
                state = it.state.trim(),
                zipCode = it.zipCode.trim()
            )
        }
    )
}
