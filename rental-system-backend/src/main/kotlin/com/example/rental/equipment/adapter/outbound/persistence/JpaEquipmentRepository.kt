package com.example.rental.equipment.adapter.outbound.persistence

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository — infrastructure concern.
 */
interface JpaEquipmentRepository : JpaRepository<EquipmentJpaEntity, Long> {
    fun findByUserId(userId: Long): List<EquipmentJpaEntity>
}

