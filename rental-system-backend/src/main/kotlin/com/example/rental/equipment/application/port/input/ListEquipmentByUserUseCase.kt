package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.application.dto.EquipmentResponse

/**
 * Input port — list equipment by user.
 */
fun interface ListEquipmentByUserUseCase {
    fun listByUser(userId: Long): List<EquipmentResponse>
}

