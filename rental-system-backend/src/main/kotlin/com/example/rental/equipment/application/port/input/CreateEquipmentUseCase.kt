package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.application.dto.CreateEquipmentCommand
import com.example.rental.equipment.application.dto.EquipmentResponse

/**
 * Input port — create a new equipment.
 */
fun interface CreateEquipmentUseCase {
    fun execute(command: CreateEquipmentCommand): EquipmentResponse
}
