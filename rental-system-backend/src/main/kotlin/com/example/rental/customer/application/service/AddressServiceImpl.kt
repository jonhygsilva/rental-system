package com.example.rental.customer.application.service

import com.example.rental.customer.application.dto.AddressResponse
import com.example.rental.customer.application.dto.CreateAddressCommand
import com.example.rental.customer.application.port.input.AddAddressUseCase
import com.example.rental.customer.application.port.input.DeleteAddressUseCase
import com.example.rental.customer.application.port.input.ListAddressesByCustomerUseCase
import com.example.rental.customer.application.port.output.AddressPersistencePort
import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.customer.domain.exception.AddressNotFoundException
import com.example.rental.customer.domain.exception.CustomerNotFoundException
import com.example.rental.customer.domain.model.Address
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddressServiceImpl(
    private val addressPersistencePort: AddressPersistencePort,
    private val customerPersistencePort: CustomerPersistencePort
) : AddAddressUseCase, ListAddressesByCustomerUseCase, DeleteAddressUseCase {

    private val log = LoggerFactory.getLogger(AddressServiceImpl::class.java)

    @Transactional
    override fun addAddress(command: CreateAddressCommand): AddressResponse {
        log.info("Adding address to customer id: {}", command.customerId)

        customerPersistencePort.findById(command.customerId)
            ?: throw CustomerNotFoundException(command.customerId.toString()).also {
                log.warn("Customer not found with id: {}", command.customerId)
            }

        val address = Address(
            street = command.street,
            number = command.number,
            complement = command.complement,
            neighborhood = command.neighborhood,
            city = command.city,
            state = command.state,
            zipCode = command.zipCode,
            customerId = command.customerId
        )

        val saved = addressPersistencePort.save(address)
        log.info("Address created with id: {} for customer: {}", saved.id, saved.customerId)
        return AddressResponse.from(saved)
    }

    @Transactional(readOnly = true)
    override fun listByCustomer(customerId: Long): List<AddressResponse> {
        log.debug("Listing addresses for customer id: {}", customerId)
        return addressPersistencePort.findByCustomerId(customerId).map { AddressResponse.from(it) }
    }

    @Transactional
    override fun deleteAddress(addressId: Long) {
        log.info("Deleting address id: {}", addressId)
        addressPersistencePort.findById(addressId)
            ?: throw AddressNotFoundException(addressId.toString()).also {
                log.warn("Address not found with id: {}", addressId)
            }
        addressPersistencePort.deleteById(addressId)
        log.info("Address deleted: {}", addressId)
    }
}

