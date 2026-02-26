package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.application.command.CreateEquipmentCommand
import com.example.rental.equipment.web.dto.EquipmentResponse

/**
 * Input port — create a new equipment.
 */
fun interface CreateEquipmentInput {
    fun execute(command: CreateEquipmentCommand): EquipmentResponse
}
