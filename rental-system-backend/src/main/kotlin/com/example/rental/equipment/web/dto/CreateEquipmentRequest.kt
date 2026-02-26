package com.example.rental.equipment.web.dto

import com.example.rental.equipment.application.command.CreateEquipmentCommand
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

/**
 * DTO received from the REST layer.
 */
data class CreateEquipmentRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:NotBlank(message = "Type is required")
    val type: String,

    val latitude: Double? = null,
    val longitude: Double? = null,

    @field:Positive(message = "Daily rate must be positive")
    val dailyRate: BigDecimal,

    @field:Positive(message = "userId must be a positive number")
    val userId: Long
) {
    fun toCommand() = CreateEquipmentCommand(
        name = name.trim(),
        type = type.trim(),
        latitude = latitude,
        longitude = longitude,
        dailyRate = dailyRate,
        userId = userId
    )
}
