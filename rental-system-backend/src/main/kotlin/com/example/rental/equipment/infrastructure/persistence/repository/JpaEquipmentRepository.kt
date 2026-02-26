package com.example.rental.equipment.infrastructure.persistence.repository

import com.example.rental.equipment.infrastructure.persistence.entity.EquipmentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository — infrastructure concern.
 */
interface JpaEquipmentRepository : JpaRepository<EquipmentJpaEntity, Long> {
    fun findByUserId(userId: Long): List<EquipmentJpaEntity>
}