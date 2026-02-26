package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.web.dto.EquipmentResponse

/**
 * Input port — list equipment by user.
 */
fun interface ListEquipmentByUserInput {
    fun listByUser(userId: Long): List<EquipmentResponse>
}

