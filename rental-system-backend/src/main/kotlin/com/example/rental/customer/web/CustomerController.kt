package com.example.rental.customer.web

import com.example.rental.customer.application.port.input.CreateCustomerInput
import com.example.rental.customer.application.port.input.DeleteCustomerInput
import com.example.rental.customer.application.port.input.GetCustomersInput
import com.example.rental.customer.application.port.input.UpdateCustomerInput
import com.example.rental.customer.web.dto.CreateCustomerRequest
import com.example.rental.customer.web.dto.CustomerResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    private val getCustomersInput: GetCustomersInput,
    private val updateCustomerInput: UpdateCustomerInput,
    private val deleteCustomerInput: DeleteCustomerInput
) {

    @PostMapping
    fun create(
        @AuthenticationPrincipal userId: Long,
        @Valid @RequestBody
        request: CreateCustomerRequest
    ): ResponseEntity<CustomerResponse> {
        val response = createCustomerInput.execute(request.toCommand(userId))
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PutMapping("/{id}")
    fun update(
        @AuthenticationPrincipal userId: Long,
        @PathVariable id: Long,
        @Valid @RequestBody
        request: CreateCustomerRequest
    ): ResponseEntity<CustomerResponse> {
        val response = updateCustomerInput.update(userId, id, request.toCommand(userId))
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getCustomers(
        @AuthenticationPrincipal userId: Long,
        @RequestParam allRequestParams: Map<String, String>
    ): ResponseEntity<Any> {
        val page = allRequestParams["page"]?.toIntOrNull() ?: 0
        val size = allRequestParams["size"]?.toIntOrNull() ?: 10
        val sort = allRequestParams["sort"] ?: "name,asc"
        val search = allRequestParams["search"]?.takeIf { it.isNotBlank() }

        val knownKeys = setOf("page", "size", "sort", "search")
        val filters = allRequestParams.filterKeys { it !in knownKeys }

        val result = getCustomersInput.getCustomers(userId, page, size, sort, search, filters)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/{id}")
    fun getCustomer(@AuthenticationPrincipal userId: Long, @PathVariable id: Long): ResponseEntity<CustomerResponse> {
        return ResponseEntity.ok(getCustomersInput.getCustomer(userId, id))
    }

    @DeleteMapping("/{id}")
    fun delete(@AuthenticationPrincipal userId: Long, @PathVariable id: Long): ResponseEntity<Void> {
        deleteCustomerInput.delete(userId, id)
        return ResponseEntity.noContent().build()
    }
}
