package com.example.rental.equipment.application.port.input

import com.example.rental.equipment.web.dto.EquipmentResponse

interface GetEquipmentsInput {
    fun getEquipment(userId: Long, id: Long): EquipmentResponse
    fun getEquipments(userId: Long): List<EquipmentResponse>
}
