package com.example.rental.equipment.application.command

import com.example.rental.equipment.domain.model.EquipmentStatus
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
    val userId: Long,
    val status: EquipmentStatus? = EquipmentStatus.DISPONIVEL
)
