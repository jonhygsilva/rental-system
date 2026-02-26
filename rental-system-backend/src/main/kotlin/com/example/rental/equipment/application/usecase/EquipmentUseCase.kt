package com.example.rental.equipment.application.usecase

import com.example.rental.equipment.application.port.input.CreateEquipmentInput
import com.example.rental.equipment.application.port.input.GetEquipmentByIdInput
import com.example.rental.equipment.application.port.input.ListEquipmentByUserInput
import com.example.rental.equipment.application.port.input.UpdateEquipmentStatusInput
import com.example.rental.equipment.application.port.output.EquipmentPersistencePort
import com.example.rental.equipment.domain.exception.EquipmentNotFoundException
import com.example.rental.equipment.domain.model.EquipmentStatus
import com.example.rental.equipment.application.command.CreateEquipmentCommand
import com.example.rental.equipment.application.port.input.UpdateEquipmentInput
import com.example.rental.equipment.domain.mapper.toDomain
import com.example.rental.equipment.web.dto.EquipmentResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class EquipmentUseCase(
    private val persistencePort: EquipmentPersistencePort
) : CreateEquipmentInput, ListEquipmentByUserInput, UpdateEquipmentStatusInput, GetEquipmentByIdInput,
    UpdateEquipmentInput {

    private val log = LoggerFactory.getLogger(EquipmentUseCase::class.java)

    @Transactional
    override fun execute(command: CreateEquipmentCommand): EquipmentResponse {
        log.info("Creating equipment: {} (type: {})", command.name, command.type)

        val saved = persistencePort.save(command.toDomain())
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
        val equipment = persistencePort.findById(id) ?: equipmentNotFound(id)

        val updated = persistencePort.save(equipment.withStatus(status))
        log.info("Equipment {} status updated to {}", id, updated.status)
        return EquipmentResponse.from(updated)
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): EquipmentResponse {
        val equipment = persistencePort.findById(id) ?: equipmentNotFound(id)
        return EquipmentResponse.from(equipment)
    }

    override fun update(
        id: Long,
        command: CreateEquipmentCommand
    ): EquipmentResponse {
        log.info("Updating equipment {} with new values", id)
        val equipment = persistencePort.findById(id) ?: equipmentNotFound(id)

        equipment.updateFrom(command)

        val updated = persistencePort.save(equipment)
        log.info("Equipment {} updated successfully", id)
        return EquipmentResponse.from(updated)
    }

    private fun equipmentNotFound(id: Long): Nothing {
        throw EquipmentNotFoundException(id.toString()).also {
            log.warn("Equipment not found with id: {}", id)
        }
    }
}