package com.example.rental.equipment.domain.model

import java.math.BigDecimal

/**
 * Pure domain entity — no framework annotations.
 * Represents a rental equipment.
 */
data class Equipment(
    val id: Long = 0,
    val name: String,
    val type: String,
    val status: EquipmentStatus = EquipmentStatus.DISPONIVEL,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dailyRate: BigDecimal,
    val userId: Long
) {
    init {
        require(name.isNotBlank()) { "Equipment name must not be blank" }
        require(type.isNotBlank()) { "Equipment type must not be blank" }
        require(dailyRate > BigDecimal.ZERO) { "Daily rate must be greater than zero" }
        require(userId > 0) { "userId must be a positive number" }
    }

    fun withStatus(newStatus: EquipmentStatus): Equipment = copy(status = newStatus)
}

