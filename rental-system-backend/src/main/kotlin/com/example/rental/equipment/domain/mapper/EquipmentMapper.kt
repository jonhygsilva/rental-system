package com.example.rental.equipment.domain.mapper

import com.example.rental.equipment.application.command.CreateEquipmentCommand
import com.example.rental.equipment.domain.model.Equipment
import com.example.rental.equipment.domain.model.EquipmentStatus
import com.example.rental.equipment.infrastructure.persistence.entity.EquipmentJpaEntity
import com.example.rental.equipment.infrastructure.persistence.entity.EquipmentJpaStatus

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

fun CreateEquipmentCommand.toDomain() = Equipment(
    name = name,
    type = type,
    latitude = latitude,
    longitude = longitude,
    dailyRate = dailyRate,
    userId = userId
)
