package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.application.command.CreateEquipmentCommand
import com.example.rental.equipment.domain.model.EquipmentStatus
import com.example.rental.equipment.web.dto.EquipmentResponse

/**
 * Input port — update equipment status.
 */
interface UpdateEquipmentsInput {
    fun update(userId: Long, id: Long, command: CreateEquipmentCommand): EquipmentResponse
    fun updateStatus(userId: Long, id: Long, status: EquipmentStatus): EquipmentResponse
}
