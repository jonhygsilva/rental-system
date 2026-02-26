package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.application.command.CreateEquipmentCommand
import com.example.rental.equipment.web.dto.EquipmentResponse

/**
 * Input port — update equipment status.
 */
fun interface UpdateEquipmentInput {
    fun update(id: Long, command: CreateEquipmentCommand): EquipmentResponse
}

