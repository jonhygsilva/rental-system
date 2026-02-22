package com.example.rental.equipment.adapter.inbound.rest

import com.example.rental.equipment.application.dto.CreateEquipmentRequest
import com.example.rental.equipment.application.dto.EquipmentResponse
import com.example.rental.equipment.application.port.input.CreateEquipmentUseCase
import com.example.rental.equipment.application.port.input.GetEquipmentByIdUseCase
import com.example.rental.equipment.application.port.input.ListEquipmentByUserUseCase
import com.example.rental.equipment.application.port.input.UpdateEquipmentStatusUseCase
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
@RequestMapping("/api/equipamentos")
class EquipmentController(
    private val createEquipmentUseCase: CreateEquipmentUseCase,
    private val listEquipmentByUserUseCase: ListEquipmentByUserUseCase,
    private val updateEquipmentStatusUseCase: UpdateEquipmentStatusUseCase,
    private val getEquipmentByIdUseCase: GetEquipmentByIdUseCase
) {

    private val log = LoggerFactory.getLogger(EquipmentController::class.java)

    @PostMapping
    fun create(@Valid @RequestBody request: CreateEquipmentRequest): ResponseEntity<EquipmentResponse> {
        log.info("POST /api/equipamentos — name: {}", request.name)
        val response = createEquipmentUseCase.execute(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun listByUser(@RequestParam userId: Long): ResponseEntity<List<EquipmentResponse>> {
        log.info("GET /api/equipamentos?userId={}", userId)
        return ResponseEntity.ok(listEquipmentByUserUseCase.listByUser(userId))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<EquipmentResponse> {
        log.info("GET /api/equipamentos/{}", id)
        return ResponseEntity.ok(getEquipmentByIdUseCase.getById(id))
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @PathVariable id: Long,
        @RequestParam status: EquipmentStatus
    ): ResponseEntity<EquipmentResponse> {
        log.info("PATCH /api/equipamentos/{}/status — status: {}", id, status)
        return ResponseEntity.ok(updateEquipmentStatusUseCase.updateStatus(id, status))
    }
}
