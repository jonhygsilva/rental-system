package com.example.rental.equipment.application.port.output

import com.example.rental.equipment.domain.model.Equipment
import com.example.rental.equipment.domain.model.EquipmentStatus

/**
 * Output port — persistence operations for Equipment.
 * Implemented by the infrastructure adapter.
 */
interface EquipmentPersistencePort {
    fun save(equipment: Equipment): Equipment
    fun findByUserId(userId: Long): List<Equipment>
    fun findByIdAndUserId(id: Long, userId: Long): Equipment?

    // returns a map of equipment status -> count for a given user
    fun countByUserIdGroupByStatus(userId: Long): Map<EquipmentStatus, Long>

    // total equipments for a user
    fun countByUserId(userId: Long): Long
}
