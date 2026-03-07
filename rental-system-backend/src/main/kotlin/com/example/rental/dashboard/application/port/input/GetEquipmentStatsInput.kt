package com.example.rental.dashboard.application.port.input

import com.example.rental.equipment.domain.model.EquipmentStatus

interface GetEquipmentStatsInput {
    fun getEquipmentStats(userId: Long): Map<EquipmentStatus, Long>
}
