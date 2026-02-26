package com.example.rental.equipment.web

import com.example.rental.customer.web.dto.CreateCustomerRequest
import com.example.rental.customer.web.dto.CustomerResponse
import com.example.rental.equipment.web.dto.CreateEquipmentRequest
import com.example.rental.equipment.web.dto.EquipmentResponse
import com.example.rental.equipment.application.port.input.CreateEquipmentInput
import com.example.rental.equipment.application.port.input.GetEquipmentByIdInput
import com.example.rental.equipment.application.port.input.ListEquipmentByUserInput
import com.example.rental.equipment.application.port.input.UpdateEquipmentInput
import com.example.rental.equipment.application.port.input.UpdateEquipmentStatusInput
import com.example.rental.equipment.domain.model.EquipmentStatus
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Inbound REST adapter — thin layer that delegates to use-case ports.
 * Contains NO business logic.
 */
@RestController
@RequestMapping("/api/equipments")
class EquipmentController(
    private val createEquipmentInput: CreateEquipmentInput,
    private val listEquipmentByUserInput: ListEquipmentByUserInput,
    private val updateEquipmentStatusInput: UpdateEquipmentStatusInput,
    private val getEquipmentByIdInput: GetEquipmentByIdInput,
    private val updateEquipmentInput: UpdateEquipmentInput
) {

    private val log = LoggerFactory.getLogger(EquipmentController::class.java)

    @PostMapping
    fun create(@Valid @RequestBody request: CreateEquipmentRequest): ResponseEntity<EquipmentResponse> {
        log.info("POST /api/equipamentos — name: {}", request.name)
        val response = createEquipmentInput.execute(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun listByUser(@RequestParam userId: Long): ResponseEntity<List<EquipmentResponse>> {
        log.info("GET /api/equipamentos?userId={}", userId)
        return ResponseEntity.ok(listEquipmentByUserInput.listByUser(userId))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<EquipmentResponse> {
        log.info("GET /api/equipamentos/{}", id)
        return ResponseEntity.ok(getEquipmentByIdInput.getById(id))
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: Long,
        @RequestParam status: EquipmentStatus
    ): ResponseEntity<EquipmentResponse> {
        log.info("PATCH /api/equipamentos/{}/status — status: {}", id, status)
        return ResponseEntity.ok(updateEquipmentStatusInput.updateStatus(id, status))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateEquipmentRequest
    ): ResponseEntity<EquipmentResponse> {
        val response = updateEquipmentInput.update(id, request.toCommand())
        return ResponseEntity.ok(response)
    }
}
