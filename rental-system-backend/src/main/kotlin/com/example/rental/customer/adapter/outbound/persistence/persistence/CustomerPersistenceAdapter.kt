package com.example.rental.customer.adapter.outbound.persistence.persistence

import com.example.rental.customer.adapter.outbound.persistence.mapper.toDomain
import com.example.rental.customer.adapter.outbound.persistence.mapper.toJpaEntity
import com.example.rental.customer.adapter.outbound.persistence.repository.JpaCustomerRepository
import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.customer.domain.model.Customer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Adapter that implements the output port using Spring Data JPA.
 */
@Component
class CustomerPersistenceAdapter(
    private val jpaRepository: JpaCustomerRepository
) : CustomerPersistencePort {

    private val log = LoggerFactory.getLogger(CustomerPersistenceAdapter::class.java)

    override fun save(customer: Customer): Customer {
        log.debug("Persisting customer: {}", customer.document)
        val saved = jpaRepository.save(customer.toJpaEntity())
        return saved.toDomain()
    }

    override fun findAll(): List<Customer> {
        log.debug("Finding all customers")
        return jpaRepository.findAll().map { it.toDomain() }
    }

    override fun findById(id: Long): Customer? {
        log.debug("Finding customer by id: {}", id)
        return jpaRepository.findById(id).orElse(null)?.toDomain()
    }

    override fun findByUserId(userId: Long): List<Customer> {
        log.debug("Finding customers by userId: {}", userId)
        return jpaRepository.findByUserId(userId).map { it.toDomain() }
    }

    override fun findByDocument(document: String): Customer? {
        log.debug("Finding customer by document: {}", document)
        return jpaRepository.findByDocument(document)?.toDomain()
    }

    override fun existsByDocument(document: String): Boolean {
        return jpaRepository.existsByDocument(document)
    }

    override fun deleteById(id: Long) {
        log.debug("Deleting customer by id: {}", id)
        jpaRepository.deleteById(id)
    }
}