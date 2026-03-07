package com.example.rental.equipment.domain.model

import com.example.rental.equipment.application.command.CreateEquipmentCommand
import java.math.BigDecimal

/**
 * Pure domain entity — no framework annotations.
 * Represents a rental equipment.
 */
data class Equipment(
    val id: Long = 0,
    var name: String,
    var type: String,
    var status: EquipmentStatus = EquipmentStatus.DISPONIVEL,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var dailyRate: BigDecimal,
    var userId: Long
) {
    init {
        require(name.isNotBlank()) { "Equipment name must not be blank" }
        require(type.isNotBlank()) { "Equipment type must not be blank" }
        require(dailyRate > BigDecimal.ZERO) { "Daily rate must be greater than zero" }
        require(userId > 0) { "userId must be a positive number" }
    }

    fun updateFrom(src: CreateEquipmentCommand) {
        name = src.name
        type = src.type
        latitude = src.latitude
        longitude = src.longitude
        dailyRate = src.dailyRate
        userId = src.userId
        status = src.status ?: status
    }

    fun withStatus(newStatus: EquipmentStatus): Equipment = copy(status = newStatus)
}
