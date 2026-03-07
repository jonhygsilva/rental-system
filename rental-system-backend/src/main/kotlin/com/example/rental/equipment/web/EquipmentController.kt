package com.example.rental.equipment.web

import com.example.rental.dashboard.application.port.input.GetEquipmentStatsInput
import com.example.rental.equipment.application.port.input.CreateEquipmentInput
import com.example.rental.equipment.application.port.input.GetEquipmentsInput
import com.example.rental.equipment.application.port.input.UpdateEquipmentsInput
import com.example.rental.equipment.domain.model.EquipmentStatus
import com.example.rental.equipment.web.dto.CreateEquipmentRequest
import com.example.rental.equipment.web.dto.EquipmentResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Inbound REST adapter — thin layer that delegates to use-case ports.
 * Contains NO business logic.
 */
@RestController
@RequestMapping("/api/equipments")
class EquipmentController(
    private val createEquipmentInput: CreateEquipmentInput,
    private val getEquipmentsInput: GetEquipmentsInput,
    private val updateEquipmentsInput: UpdateEquipmentsInput,
    private val getEquipmentStatsInput: GetEquipmentStatsInput
) {

    private val log = LoggerFactory.getLogger(EquipmentController::class.java)

    @PostMapping
    fun create(
        @AuthenticationPrincipal userId: Long,
        @Valid @RequestBody
        request: CreateEquipmentRequest
    ): ResponseEntity<EquipmentResponse> {
        log.info("POST /api/equipments — name: {}", request.name)
        val response = createEquipmentInput.execute(request.toCommand(userId))
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getEquipments(@AuthenticationPrincipal userId: Long): ResponseEntity<List<EquipmentResponse>> {
        log.info("GET /api/equipamentos?userId={}", userId)
        return ResponseEntity.ok(getEquipmentsInput.getEquipments(userId))
    }

    @GetMapping("/{id}")
    fun getEquipment(@AuthenticationPrincipal userId: Long, @PathVariable id: Long): ResponseEntity<EquipmentResponse> {
        log.info("GET /api/equipamentos/{}", id)
        return ResponseEntity.ok(getEquipmentsInput.getEquipment(userId, id))
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @AuthenticationPrincipal userId: Long,
        @PathVariable id: Long,
        @RequestBody status: EquipmentStatus
    ): ResponseEntity<EquipmentResponse> {
        log.info("PATCH /api/equipamentos/{}/status — status: {}", id, status)
        return ResponseEntity.ok(updateEquipmentsInput.updateStatus(userId, id, status))
    }

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal userId: Long,
        @PathVariable id: Long,
        @Valid @RequestBody
        request: CreateEquipmentRequest
    ): ResponseEntity<EquipmentResponse> {
        val response = updateEquipmentsInput.update(userId, id, request.toCommand(userId))
        return ResponseEntity.ok(response)
    }
}
