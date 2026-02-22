package com.example.rental.equipment.application.dto

import java.math.BigDecimal

/**
 * Internal command — framework-agnostic representation of the create-equipment intent.
 */
data class CreateEquipmentCommand(
    val name: String,
    val type: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dailyRate: BigDecimal,
    val userId: Long
)

