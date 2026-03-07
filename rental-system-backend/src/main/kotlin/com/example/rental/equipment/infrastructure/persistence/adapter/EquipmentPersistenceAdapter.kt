package com.example.rental.equipment.infrastructure.persistence.adapter

import com.example.rental.equipment.application.port.output.EquipmentPersistencePort
import com.example.rental.equipment.domain.mapper.toDomain
import com.example.rental.equipment.domain.mapper.toJpaEntity
import com.example.rental.equipment.domain.model.Equipment
import com.example.rental.equipment.domain.model.EquipmentStatus
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
        val saved = jpaRepository.save(equipment.toJpaEntity())
        return saved.toDomain()
    }

    override fun findByIdAndUserId(id: Long, userId: Long): Equipment? {
        log.debug("Finding equipment by id: {}", id)
        return jpaRepository.findByIdAndUserId(id, userId)?.toDomain()
    }

    override fun findByUserId(userId: Long): List<Equipment> {
        log.debug("Finding equipment by userId: {}", userId)
        return jpaRepository.findByUserId(userId).map { it.toDomain() }
    }

    override fun countByUserIdGroupByStatus(userId: Long): Map<EquipmentStatus, Long> {
        val rows = jpaRepository.countByStatusForUser(userId)
        val result = mutableMapOf<EquipmentStatus, Long>()
        rows.forEach { row ->
            if (row.size >= 2) {
                val status = (row[0] as? Enum<*>)?.name
                val count = (row[1] as? Number)?.toLong() ?: 0L
                if (status != null) {
                    val domainStatus = EquipmentStatus.valueOf(status)
                    result[domainStatus] = count
                }
            }
        }
        return result
    }

    override fun countByUserId(userId: Long): Long {
        return jpaRepository.countByUserId(userId)
    }
}
