package com.example.rental.customer.infrastructure.persistence.adapter

import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.customer.domain.mapper.toDomain
import com.example.rental.customer.domain.mapper.toJpaEntity
import com.example.rental.customer.domain.model.Customer
import com.example.rental.customer.infrastructure.persistence.repository.JpaCustomerRepository
import org.springframework.stereotype.Component

@Component
class CustomerPersistenceAdapter(
    private val jpaRepository: JpaCustomerRepository
) : CustomerPersistencePort {

    override fun save(customer: Customer): Customer {
        val saved = jpaRepository.save(customer.toJpaEntity())
        return saved.toDomain()
    }

    override fun findById(id: Long): Customer? =
        jpaRepository.findById(id).orElse(null)?.toDomain()

    override fun findByUserId(userId: Long): List<Customer> =
        jpaRepository.findByUserId(userId).map { it.toDomain() }

    override fun existsByDocument(document: String): Boolean =
        jpaRepository.existsByDocument(document)

    override fun deleteById(id: Long) =
        jpaRepository.deleteById(id)
}