package com.example.rental.equipment.application.usecase

import com.example.rental.equipment.application.command.CreateEquipmentCommand
import com.example.rental.equipment.application.port.input.CreateEquipmentInput
import com.example.rental.equipment.application.port.input.GetEquipmentsInput
import com.example.rental.equipment.application.port.input.UpdateEquipmentsInput
import com.example.rental.equipment.application.port.output.EquipmentPersistencePort
import com.example.rental.equipment.domain.exception.EquipmentNotFoundException
import com.example.rental.equipment.domain.mapper.toDomain
import com.example.rental.equipment.domain.model.EquipmentStatus
import com.example.rental.equipment.web.dto.EquipmentResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EquipmentUseCase(
    private val persistencePort: EquipmentPersistencePort
) : CreateEquipmentInput, GetEquipmentsInput, UpdateEquipmentsInput {

    private val log = LoggerFactory.getLogger(EquipmentUseCase::class.java)

    @Transactional
    override fun execute(command: CreateEquipmentCommand): EquipmentResponse {
        log.info("Creating equipment: {} (type: {})", command.name, command.type)

        val saved = persistencePort.save(command.toDomain())
        log.info("Equipment created successfully with id: {}", saved.id)
        return EquipmentResponse.from(saved)
    }

    @Transactional(readOnly = true)
    override fun getEquipments(userId: Long): List<EquipmentResponse> {
        return persistencePort.findByUserId(userId).map { EquipmentResponse.from(it) }
    }

    @Transactional
    override fun updateStatus(userId: Long, id: Long, status: EquipmentStatus): EquipmentResponse {
        log.info("Updating equipment {} status to {}", id, status)
        val equipment = persistencePort.findByIdAndUserId(id, userId) ?: equipmentNotFound(id)

        val updated = persistencePort.save(equipment.withStatus(status))
        log.info("Equipment {} status updated to {}", id, updated.status)
        return EquipmentResponse.from(updated)
    }

    @Transactional(readOnly = true)
    override fun getEquipment(userId: Long, id: Long): EquipmentResponse {
        val equipment = persistencePort.findByIdAndUserId(id, userId) ?: equipmentNotFound(id)
        return EquipmentResponse.from(equipment)
    }

    override fun update(
        userId: Long,
        id: Long,
        command: CreateEquipmentCommand
    ): EquipmentResponse {
        log.info("Updating equipment {} with new values", id)
        val equipment = persistencePort.findByIdAndUserId(id, userId) ?: equipmentNotFound(id)

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
