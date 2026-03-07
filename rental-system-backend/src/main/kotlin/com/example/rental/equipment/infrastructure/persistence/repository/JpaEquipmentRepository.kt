package com.example.rental.equipment.infrastructure.persistence.repository

import com.example.rental.equipment.infrastructure.persistence.entity.EquipmentJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * Spring Data JPA repository — infrastructure concern.
 */
interface JpaEquipmentRepository : JpaRepository<EquipmentJpaEntity, Long> {
    fun findByUserId(userId: Long): List<EquipmentJpaEntity>
    fun findByIdAndUserId(id: Long, userId: Long): EquipmentJpaEntity?

    @Query("SELECT e.status, COUNT(e) FROM EquipmentJpaEntity e WHERE e.userId = :userId GROUP BY e.status")
    fun countByStatusForUser(userId: Long): List<Array<Any>>

    fun countByUserId(userId: Long): Long
}
