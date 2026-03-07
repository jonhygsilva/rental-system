package com.example.rental.equipment.web.dto

import com.example.rental.equipment.domain.model.Equipment
import com.example.rental.equipment.domain.model.EquipmentStatus
import java.math.BigDecimal

/**
 * DTO returned to the REST layer.
 */
data class EquipmentResponse(
    val id: Long,
    val name: String,
    val type: String,
    val status: EquipmentStatus,
    val latitude: Double?,
    val longitude: Double?,
    val dailyRate: BigDecimal
) {
    companion object {
        fun from(equipment: Equipment) = EquipmentResponse(
            id = equipment.id,
            name = equipment.name,
            type = equipment.type,
            status = equipment.status,
            latitude = equipment.latitude,
            longitude = equipment.longitude,
            dailyRate = equipment.dailyRate
        )
    }
}
