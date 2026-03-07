package com.example.rental.dashboard.domain.model

import com.example.rental.equipment.domain.model.EquipmentStatus
import com.example.rental.rental.domain.model.RentalStatus

data class DashboardData(
    val totalCustomers: Long,
    val equipmentByStatus: Map<EquipmentStatus, Long>,
    val rentalsByStatus: Map<RentalStatus, Long>
)
