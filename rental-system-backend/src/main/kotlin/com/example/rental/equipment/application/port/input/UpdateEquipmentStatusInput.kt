package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.web.dto.EquipmentResponse
import com.example.rental.equipment.domain.model.EquipmentStatus

/**
 * Input port — update equipment status.
 */
fun interface UpdateEquipmentStatusInput {
    fun updateStatus(id: Long, status: EquipmentStatus): EquipmentResponse
}

