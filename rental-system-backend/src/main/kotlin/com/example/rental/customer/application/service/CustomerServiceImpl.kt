package com.example.rental.customer.application.service

import com.example.rental.customer.application.dto.AddressResponse
import com.example.rental.customer.application.dto.CreateCustomerCommand
import com.example.rental.customer.application.dto.CustomerResponse
import com.example.rental.customer.application.port.input.CreateCustomerUseCase
import com.example.rental.customer.application.port.input.DeleteCustomerUseCase
import com.example.rental.customer.application.port.input.GetCustomerByIdUseCase
import com.example.rental.customer.application.port.input.ListCustomersByUserUseCase
import com.example.rental.customer.application.port.input.UpdateCustomerUseCase
import com.example.rental.customer.application.port.output.AddressPersistencePort
import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.customer.domain.exception.CustomerNotFoundException
import com.example.rental.customer.domain.exception.DuplicateDocumentException
import com.example.rental.customer.domain.model.Customer
import com.example.rental.customer.domain.model.Address
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Application service — orchestrates use cases using ports.
 * Contains NO infrastructure logic (no JPA, no HTTP).
 */
@Service
class CustomerServiceImpl(
    private val persistencePort: CustomerPersistencePort,
    private val addressPersistencePort: AddressPersistencePort
) : CreateCustomerUseCase, ListCustomersByUserUseCase, GetCustomerByIdUseCase, UpdateCustomerUseCase, DeleteCustomerUseCase {

    private val log = LoggerFactory.getLogger(CustomerServiceImpl::class.java)

    @Transactional
    override fun execute(command: CreateCustomerCommand): CustomerResponse {
        log.info("Creating customer with document: {}", command.document)

        if (persistencePort.existsByDocument(command.document)) {
            log.warn("Attempt to create customer with duplicate document: {}", command.document)
            throw DuplicateDocumentException(command.document)
        }

        val domainCustomer = Customer(
            name = command.name,
            document = command.document,
            phone = command.phone,
            userId = command.userId
        )

        val saved = persistencePort.save(domainCustomer)
        log.info("Customer created successfully with id: {}", saved.id)

        val savedAddresses = command.addresses.map { addrCmd ->
            val address = Address(
                street = addrCmd.street,
                number = addrCmd.number,
                complement = addrCmd.complement,
                neighborhood = addrCmd.neighborhood,
                city = addrCmd.city,
                state = addrCmd.state,
                zipCode = addrCmd.zipCode,
                customerId = saved.id
            )
            addressPersistencePort.save(address)
        }

        val addressResponses = savedAddresses.map { AddressResponse.from(it) }
        log.info("Created {} addresses for customer id: {}", savedAddresses.size, saved.id)
        return CustomerResponse.from(saved, addressResponses)
    }

    @Transactional(readOnly = true)
    override fun listByUser(userId: Long): List<CustomerResponse> {
        log.debug("Listing customers for userId: {}", userId)
        return persistencePort.findByUserId(userId).map { customer ->
            val addresses = addressPersistencePort.findByCustomerId(customer.id)
                .map { AddressResponse.from(it) }
            CustomerResponse.from(customer, addresses)
        }
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): CustomerResponse {
        log.debug("Fetching customer by id: {}", id)
        val customer = persistencePort.findById(id)
            ?: throw CustomerNotFoundException(id.toString()).also {
                log.warn("Customer not found with id: {}", id)
            }
        val addresses = addressPersistencePort.findByCustomerId(customer.id)
            .map { AddressResponse.from(it) }
        return CustomerResponse.from(customer, addresses)
    }

    @Transactional
    override fun update(id: Long, command: CreateCustomerCommand): CustomerResponse {
        log.info("Updating customer id: {}", id)

        val existing = persistencePort.findById(id)
            ?: throw CustomerNotFoundException(id.toString()).also {
                log.warn("Customer not found with id: {}", id)
            }

        // Check duplicate document (if changed)
        if (existing.document != command.document && persistencePort.existsByDocument(command.document)) {
            log.warn("Attempt to update customer with duplicate document: {}", command.document)
            throw DuplicateDocumentException(command.document)
        }

        val updatedCustomer = Customer(
            id = existing.id,
            name = command.name,
            document = command.document,
            phone = command.phone,
            userId = command.userId
        )

        val savedCustomer = persistencePort.save(updatedCustomer)

        // Replace addresses: delete old ones and save new set
        val oldAddresses = addressPersistencePort.findByCustomerId(savedCustomer.id)
        oldAddresses.forEach { addressPersistencePort.deleteById(it.id) }

        val newSaved = command.addresses.map { addrCmd ->
            val address = Address(
                street = addrCmd.street,
                number = addrCmd.number,
                complement = addrCmd.complement,
                neighborhood = addrCmd.neighborhood,
                city = addrCmd.city,
                state = addrCmd.state,
                zipCode = addrCmd.zipCode,
                customerId = savedCustomer.id
            )
            addressPersistencePort.save(address)
        }

        val addressResponses = newSaved.map { AddressResponse.from(it) }
        log.info("Updated customer id: {} with {} addresses", savedCustomer.id, newSaved.size)
        return CustomerResponse.from(savedCustomer, addressResponses)
    }

    @Transactional
    override fun delete(id: Long) {
        log.info("Deleting customer with id: {}", id)

        val customer = persistencePort.findById(id)
            ?: throw CustomerNotFoundException(id.toString()).also {
                log.warn("Customer not found with id: {}", id)
            }

        val addresses = addressPersistencePort.findByCustomerId(customer.id)
        addresses.forEach { address ->
            addressPersistencePort.deleteById(address.id)
            log.debug("Deleted address with id: {} for customer id: {}", address.id, customer.id)
        }

        persistencePort.deleteById(customer.id)
        log.info("Deleted customer with id: {}", customer.id)
    }
}
