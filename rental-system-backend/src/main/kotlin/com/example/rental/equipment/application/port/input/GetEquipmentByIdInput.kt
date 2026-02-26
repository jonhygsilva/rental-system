package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.web.dto.EquipmentResponse

/**
 * Input port — find equipment by id.
 */
fun interface GetEquipmentByIdInput {
    fun getById(id: Long): EquipmentResponse
}

