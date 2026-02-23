package com.example.rental.customer.web

import com.example.rental.customer.web.dto.CreateCustomerRequest
import com.example.rental.customer.web.dto.CustomerResponse
import com.example.rental.customer.application.port.input.CreateCustomerInput
import com.example.rental.customer.application.port.input.DeleteCustomerInput
import com.example.rental.customer.application.port.input.GetCustomerByIdInput
import com.example.rental.customer.application.port.input.ListCustomersByUserInput
import com.example.rental.customer.application.port.input.UpdateCustomerInput
import jakarta.validation.Valid
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

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val createCustomerInput: CreateCustomerInput,
    private val listCustomersByUserInput: ListCustomersByUserInput,
    private val getCustomerByIdInput: GetCustomerByIdInput,
    private val updateCustomerInput: UpdateCustomerInput,
    private val deleteCustomerInput: DeleteCustomerInput
) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreateCustomerRequest): ResponseEntity<CustomerResponse> {
        val response = createCustomerInput.execute(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateCustomerRequest
    ): ResponseEntity<CustomerResponse> {
        val response = updateCustomerInput.update(id, request.toCommand())
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun listByUser(@RequestParam userId: Long): ResponseEntity<List<CustomerResponse>> {
        return ResponseEntity.ok(listCustomersByUserInput.listByUser(userId))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<CustomerResponse> {
        return ResponseEntity.ok(getCustomerByIdInput.getById(id))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        deleteCustomerInput.delete(id)
        return ResponseEntity.noContent().build()
    }
}