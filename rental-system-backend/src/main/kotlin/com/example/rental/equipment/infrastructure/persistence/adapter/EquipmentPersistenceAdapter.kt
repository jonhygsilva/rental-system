package com.example.rental.equipment.infrastructure.persistence.adapter

import com.example.rental.equipment.application.port.output.EquipmentPersistencePort
import com.example.rental.equipment.domain.mapper.toDomain
import com.example.rental.equipment.domain.mapper.toJpaEntity
import com.example.rental.equipment.domain.model.Equipment
import com.example.rental.equipment.infrastructure.persistence.repository.JpaEquipmentRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Adapter that implements the output port using Spring Data JPA.
 */
@Component
class EquipmentPersistenceAdapter(
    private val jpaRepository: JpaEquipmentRepository
) : EquipmentPersistencePort {

    private val log = LoggerFactory.getLogger(EquipmentPersistenceAdapter::class.java)

    override fun save(equipment: Equipment): Equipment {
        log.debug("Persisting equipment: {}", equipment.name)
        val saved = jpaRepository.save(equipment.toJpaEntity())
        return saved.toDomain()
    }

    override fun findAll(): List<Equipment> {
        log.debug("Finding all equipment")
        return jpaRepository.findAll().map { it.toDomain() }
    }

    override fun findById(id: Long): Equipment? {
        log.debug("Finding equipment by id: {}", id)
        return jpaRepository.findById(id).orElse(null)?.toDomain()
    }

    override fun findByUserId(userId: Long): List<Equipment> {
        log.debug("Finding equipment by userId: {}", userId)
        return jpaRepository.findByUserId(userId).map { it.toDomain() }
    }
}