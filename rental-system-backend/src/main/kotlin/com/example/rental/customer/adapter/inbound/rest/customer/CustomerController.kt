package com.example.rental.customer.adapter.inbound.rest.customer

import com.example.rental.customer.application.dto.CreateCustomerRequest
import com.example.rental.customer.application.dto.CustomerResponse
import com.example.rental.customer.application.port.input.CreateCustomerUseCase
import com.example.rental.customer.application.port.input.DeleteCustomerUseCase
import com.example.rental.customer.application.port.input.GetCustomerByIdUseCase
import com.example.rental.customer.application.port.input.ListCustomersByUserUseCase
import com.example.rental.customer.application.port.input.UpdateCustomerUseCase
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Inbound REST adapter — customer management.
 * Customer responses always include their addresses.
 */
@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val createCustomerUseCase: CreateCustomerUseCase,
    private val listCustomersByUserUseCase: ListCustomersByUserUseCase,
    private val getCustomerByIdUseCase: GetCustomerByIdUseCase,
    private val updateCustomerUseCase: UpdateCustomerUseCase,
    private val deleteCustomerUseCase: DeleteCustomerUseCase
) {

    private val log = LoggerFactory.getLogger(CustomerController::class.java)

    @PostMapping
    fun create(@Valid @RequestBody request: CreateCustomerRequest): ResponseEntity<CustomerResponse> {
        log.info("POST /api/customers — document: {}", request.document)
        val response = createCustomerUseCase.execute(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateCustomerRequest
    ): ResponseEntity<CustomerResponse> {
        log.info("PUT /api/customers/{} — document: {}", id, request.document)
        val response = updateCustomerUseCase.update(id, request.toCommand())
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun listByUser(@RequestParam userId: Long): ResponseEntity<List<CustomerResponse>> {
        log.info("GET /api/customers?userId={}", userId)
        return ResponseEntity.ok(listCustomersByUserUseCase.listByUser(userId))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<CustomerResponse> {
        log.info("GET /api/customers/{}", id)
        return ResponseEntity.ok(getCustomerByIdUseCase.getById(id))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        log.info("DELETE /api/customers/{}", id)
        deleteCustomerUseCase.delete(id)
        return ResponseEntity.noContent().build()
    }
}