package com.example.rental.customer.application.usecase

import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.customer.domain.exception.CustomerNotFoundException
import com.example.rental.customer.domain.mapper.toDomain
import com.example.rental.customer.application.port.input.CreateCustomerInput
import com.example.rental.customer.application.port.input.DeleteCustomerInput
import com.example.rental.customer.application.port.input.GetCustomerByIdInput
import com.example.rental.customer.application.port.input.ListCustomersByUserInput
import com.example.rental.customer.application.port.input.UpdateCustomerInput
import com.example.rental.customer.application.command.CreateCustomerCommand
import com.example.rental.customer.web.dto.CustomerResponse
import com.example.rental.customer.application.usecase.validations.CustomerDocumentUniquenessCheckerImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerUseCase(
    private val persistencePort: CustomerPersistencePort
) : CreateCustomerInput,
    ListCustomersByUserInput,
    GetCustomerByIdInput,
    UpdateCustomerInput,
    DeleteCustomerInput {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun execute(command: CreateCustomerCommand): CustomerResponse {
        log.info("Creating customer with document: {}", command.document)

        val domainCustomer = command.toDomain()
        domainCustomer.documentAlreadyExists(CustomerDocumentUniquenessCheckerImpl(persistencePort))

        val saved = persistencePort.save(domainCustomer)

        log.info("Customer created successfully with id: {}", saved.id)

        return CustomerResponse.from(saved)
    }

    @Transactional(readOnly = true)
    override fun listByUser(userId: Long): List<CustomerResponse> {
        log.debug("Listing customers for userId: {}", userId)

        return persistencePort.findByUserId(userId).map { CustomerResponse.from(it) }
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): CustomerResponse {
        log.debug("Fetching customer by id: {}", id)

        val customer = persistencePort.findById(id)
            ?: customerNotFound(id)

        return CustomerResponse.from(customer)
    }

    @Transactional
    override fun update(id: Long, command: CreateCustomerCommand): CustomerResponse {

        val customer = persistencePort.findById(id)
            ?: customerNotFound(id)

        customer.changeDocument(
            command.document,
            CustomerDocumentUniquenessCheckerImpl(persistencePort)
        )

        customer.updateFrom(command)

        val saved = persistencePort.save(customer)

        return CustomerResponse.from(saved,)
    }

    @Transactional
    override fun delete(id: Long) {
        log.info("Deleting customer with id: {}", id)

        val customer = persistencePort.findById(id)
            ?: customerNotFound(id)

        persistencePort.deleteById(id)

        log.info("Deleted customer with id: {}", customer.id)
    }

    private fun customerNotFound(id: Long): Nothing {
        log.warn("Customer not found with id: {}", id)
        throw CustomerNotFoundException(id.toString())
    }
}