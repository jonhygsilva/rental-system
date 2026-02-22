package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.application.dto.EquipmentResponse
import com.example.rental.equipment.domain.model.EquipmentStatus

/**
 * Input port — update equipment status.
 */
fun interface UpdateEquipmentStatusUseCase {
    fun updateStatus(id: Long, status: EquipmentStatus): EquipmentResponse
}

