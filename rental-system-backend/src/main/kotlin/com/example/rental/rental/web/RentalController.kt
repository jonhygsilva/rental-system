package com.example.rental.rental.web

import com.example.rental.rental.application.port.input.CreateRentalInput
import com.example.rental.rental.application.port.input.GetRentalStatsInput
import com.example.rental.rental.application.port.input.GetRentalsInput
import com.example.rental.rental.application.port.input.UpdateRentalInput
import com.example.rental.rental.web.dto.CreateRentalRequest
import com.example.rental.rental.web.dto.RentalResponse
import com.example.rental.rental.web.dto.UpdateRentalStatusRequest
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

@RestController
@RequestMapping("/api/rentals")
class RentalController(
    private val createRentalInput: CreateRentalInput,
    private val getRentalsInput: GetRentalsInput,
    private val updateRentalInput: UpdateRentalInput,
    private val getRentalStatsInput: GetRentalStatsInput
) {

    @PostMapping
    fun create(@AuthenticationPrincipal userId: Long, @RequestBody rental: CreateRentalRequest): ResponseEntity<RentalResponse> {
        val response = createRentalInput.execute(rental.toCommand(userId))
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PutMapping("/{id}")
    fun update(@AuthenticationPrincipal userId: Long, @PathVariable id: Long, @RequestBody rental: CreateRentalRequest): ResponseEntity<RentalResponse> {
        val response = updateRentalInput.update(id, rental.toCommand(userId))
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getRentals(@AuthenticationPrincipal userId: Long): ResponseEntity<List<RentalResponse>> {
        return ResponseEntity.ok(getRentalsInput.getRentals(userId))
    }

    @GetMapping("/stats")
    fun getStats(@AuthenticationPrincipal userId: Long): ResponseEntity<Map<com.example.rental.rental.domain.model.RentalStatus, Long>> {
        val stats = getRentalStatsInput.getStats(userId)
        return ResponseEntity.ok(stats)
    }

    @GetMapping("/{id}")
    fun getRental(@AuthenticationPrincipal userId: Long, @PathVariable id: Long): ResponseEntity<RentalResponse> {
        return ResponseEntity.ok(getRentalsInput.getRental(userId, id))
    }

    @PatchMapping("/{id}/status")
    fun updateStatus(
        @AuthenticationPrincipal userId: Long,
        @PathVariable id: Long,
        @RequestBody input: UpdateRentalStatusRequest
    ): ResponseEntity<RentalResponse> {
        return ResponseEntity.ok(updateRentalInput.updateStatus(userId, id, input.status))
    }
}
