package com.example.rental.equipment.application.port.output

import com.example.rental.equipment.domain.model.Equipment

/**
 * Output port — persistence operations for Equipment.
 * Implemented by the infrastructure adapter.
 */
interface EquipmentPersistencePort {
    fun save(equipment: Equipment): Equipment
    fun findAll(): List<Equipment>
    fun findById(id: Long): Equipment?
    fun findByUserId(userId: Long): List<Equipment>
}

