package com.example.rental.equipment.application.service

import com.example.rental.equipment.application.dto.CreateEquipmentCommand
import com.example.rental.equipment.application.dto.EquipmentResponse
import com.example.rental.equipment.application.port.input.CreateEquipmentUseCase
import com.example.rental.equipment.application.port.input.GetEquipmentByIdUseCase
import com.example.rental.equipment.application.port.input.ListEquipmentByUserUseCase
import com.example.rental.equipment.application.port.input.UpdateEquipmentStatusUseCase
import com.example.rental.equipment.application.port.output.EquipmentPersistencePort
import com.example.rental.equipment.domain.exception.EquipmentNotFoundException
import com.example.rental.equipment.domain.model.Equipment
import com.example.rental.equipment.domain.model.EquipmentStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Application service — orchestrates use cases using ports.
 * Contains NO infrastructure logic (no JPA, no HTTP).
 */
@Service
class EquipmentServiceImpl(
    private val persistencePort: EquipmentPersistencePort
) : CreateEquipmentUseCase, ListEquipmentByUserUseCase, UpdateEquipmentStatusUseCase, GetEquipmentByIdUseCase {

    private val log = LoggerFactory.getLogger(EquipmentServiceImpl::class.java)

    @Transactional
    override fun execute(command: CreateEquipmentCommand): EquipmentResponse {
        log.info("Creating equipment: {} (type: {})", command.name, command.type)

        val domainEquipment = Equipment(
            name = command.name,
            type = command.type,
            latitude = command.latitude,
            longitude = command.longitude,
            dailyRate = command.dailyRate,
            userId = command.userId
        )

        val saved = persistencePort.save(domainEquipment)
        log.info("Equipment created successfully with id: {}", saved.id)
        return EquipmentResponse.from(saved)
    }

    @Transactional(readOnly = true)
    override fun listByUser(userId: Long): List<EquipmentResponse> {
        log.debug("Listing equipment for userId: {}", userId)
        return persistencePort.findByUserId(userId).map { EquipmentResponse.from(it) }
    }

    @Transactional
    override fun updateStatus(id: Long, status: EquipmentStatus): EquipmentResponse {
        log.info("Updating equipment {} status to {}", id, status)
        val equipment = persistencePort.findById(id)
            ?: throw EquipmentNotFoundException(id.toString()).also {
                log.warn("Equipment not found with id: {}", id)
            }

        val updated = persistencePort.save(equipment.withStatus(status))
        log.info("Equipment {} status updated to {}", id, updated.status)
        return EquipmentResponse.from(updated)
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): EquipmentResponse {
        log.debug("Fetching equipment by id: {}", id)
        val equipment = persistencePort.findById(id)
            ?: throw EquipmentNotFoundException(id.toString()).also {
                log.warn("Equipment not found with id: {}", id)
            }
        return EquipmentResponse.from(equipment)
    }
}

