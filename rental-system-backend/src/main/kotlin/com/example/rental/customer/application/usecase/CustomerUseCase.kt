package com.example.rental.customer.application.usecase

import com.example.rental.common.web.dto.PaginatedResponse
import com.example.rental.customer.application.command.CreateCustomerCommand
import com.example.rental.customer.application.port.input.CountCustomersByPeriodInput
import com.example.rental.customer.application.port.input.CreateCustomerInput
import com.example.rental.customer.application.port.input.DeleteCustomerInput
import com.example.rental.customer.application.port.input.GetCustomersInput
import com.example.rental.customer.application.port.input.UpdateCustomerInput
import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.customer.application.usecase.validations.CustomerDocumentUniquenessCheckerImpl
import com.example.rental.customer.domain.exception.CustomerNotFoundException
import com.example.rental.customer.domain.mapper.toDomain
import com.example.rental.customer.web.dto.CustomerResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CustomerUseCase(
    private val persistencePort: CustomerPersistencePort
) : CreateCustomerInput,
    GetCustomersInput,
    UpdateCustomerInput,
    DeleteCustomerInput,
    CountCustomersByPeriodInput {

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
    override fun getCustomers(userId: Long, page: Int, size: Int, sort: String, search: String?, filters: Map<String, String>): PaginatedResponse<CustomerResponse> {
        val parts = sort.split(",")
        val field = parts.getOrNull(0) ?: "name"
        val dir = parts.getOrNull(1)?.lowercase() ?: "asc"
        val sortObj = if (dir == "desc") Sort.by(Sort.Direction.DESC, field) else Sort.by(Sort.Direction.ASC, field)
        val pageable = PageRequest.of(page, size, sortObj)

        val documentFilter = filters["document"]
        val phoneFilter = filters["phone"]

        val pageResult = persistencePort.findByUserIdWithFilters(userId, search, documentFilter, phoneFilter, pageable)

        return PaginatedResponse(
            content = pageResult.content.map { CustomerResponse.from(it) },
            page = pageResult.number,
            size = pageResult.size,
            totalElements = pageResult.totalElements,
            totalPages = pageResult.totalPages
        )
    }

    @Transactional(readOnly = true)
    override fun getCustomer(userId: Long, id: Long): CustomerResponse {
        log.debug("Fetching customer by id: {}", id)

        val customer = persistencePort.findByIdAndUserId(id, userId)
            ?: customerNotFound(id)

        return CustomerResponse.from(customer)
    }

    @Transactional
    override fun update(userId: Long, id: Long, command: CreateCustomerCommand): CustomerResponse {
        val customer = persistencePort.findByIdAndUserId(id, userId)
            ?: customerNotFound(id)

        customer.changeDocument(
            command.document,
            CustomerDocumentUniquenessCheckerImpl(persistencePort)
        )

        customer.updateFrom(command)

        val saved = persistencePort.save(customer)

        return CustomerResponse.from(saved)
    }

    @Transactional
    override fun delete(userId: Long, id: Long) {
        log.info("Deleting customer with id: {}", id)

        val customer = persistencePort.findByIdAndUserId(id, userId)
            ?: customerNotFound(id)

        persistencePort.deleteByIdAndUserId(id, userId)

        log.info("Deleted customer with id: {}", customer.id)
    }

    @Transactional(readOnly = true)
    override fun count(userId: Long, start: LocalDateTime, end: LocalDateTime): Long {
        log.debug("Counting customers for user {} between {} and {}", userId, start, end)
        return persistencePort.countByUserIdAndCreatedAtBetween(userId, start, end)
    }

    private fun customerNotFound(id: Long): Nothing {
        log.warn("Customer not found with id: {}", id)
        throw CustomerNotFoundException(id.toString())
    }
}
