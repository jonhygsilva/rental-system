package com.example.rental.equipment.adapter.outbound.persistence

import com.example.rental.equipment.domain.model.Equipment
import com.example.rental.equipment.domain.model.EquipmentStatus

/**
 * Extension functions to map between domain model and JPA entity.
 */
fun Equipment.toJpaEntity() = EquipmentJpaEntity(
    id = id,
    name = name,
    type = type,
    status = EquipmentJpaStatus.valueOf(status.name),
    latitude = latitude,
    longitude = longitude,
    dailyRate = dailyRate,
    userId = userId
)

fun EquipmentJpaEntity.toDomain() = Equipment(
    id = id,
    name = name,
    type = type,
    status = EquipmentStatus.valueOf(status.name),
    latitude = latitude,
    longitude = longitude,
    dailyRate = dailyRate,
    userId = userId
)

