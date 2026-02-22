package com.example.rental.customer.adapter.inbound.rest.address

import com.example.rental.customer.application.dto.AddressResponse
import com.example.rental.customer.application.dto.CreateAddressRequest
import com.example.rental.customer.application.port.input.AddAddressUseCase
import com.example.rental.customer.application.port.input.DeleteAddressUseCase
import com.example.rental.customer.application.port.input.ListAddressesByCustomerUseCase
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Inbound REST adapter — address management.
 * Endpoints nested under /api/customers/{customerId}/addresses.
 */
@RestController
@RequestMapping("/api/customers/{customerId}/addresses")
class AddressController(
    private val addAddressUseCase: AddAddressUseCase,
    private val listAddressesByCustomerUseCase: ListAddressesByCustomerUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase
) {

    private val log = LoggerFactory.getLogger(AddressController::class.java)

    @PostMapping
    fun addAddress(
        @PathVariable customerId: Long,
        @Valid @RequestBody request: CreateAddressRequest
    ): ResponseEntity<AddressResponse> {
        log.info("POST /api/customers/{}/addresses", customerId)
        val response = addAddressUseCase.addAddress(request.toCommand(customerId))
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun listAddresses(@PathVariable customerId: Long): ResponseEntity<List<AddressResponse>> {
        log.info("GET /api/customers/{}/addresses", customerId)
        return ResponseEntity.ok(listAddressesByCustomerUseCase.listByCustomer(customerId))
    }

    @DeleteMapping("/{addressId}")
    fun deleteAddress(
        @PathVariable customerId: Long,
        @PathVariable addressId: Long
    ): ResponseEntity<Void> {
        log.info("DELETE /api/customers/{}/addresses/{}", customerId, addressId)
        deleteAddressUseCase.deleteAddress(addressId)
        return ResponseEntity.noContent().build()
    }
}