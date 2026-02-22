package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.application.dto.EquipmentResponse

/**
 * Input port — find equipment by id.
 */
fun interface GetEquipmentByIdUseCase {
    fun getById(id: Long): EquipmentResponse
}

