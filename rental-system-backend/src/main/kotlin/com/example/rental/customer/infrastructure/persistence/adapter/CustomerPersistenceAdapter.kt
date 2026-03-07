package com.example.rental.customer.infrastructure.persistence.adapter

import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.customer.domain.mapper.toDomain
import com.example.rental.customer.domain.mapper.toJpaEntity
import com.example.rental.customer.domain.model.Customer
import com.example.rental.customer.infrastructure.persistence.repository.JpaCustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CustomerPersistenceAdapter(
    private val jpaRepository: JpaCustomerRepository
) : CustomerPersistencePort {

    override fun save(customer: Customer): Customer {
        val saved = jpaRepository.save(customer.toJpaEntity())
        return saved.toDomain()
    }

    override fun findByIdAndUserId(id: Long, userId: Long): Customer? =
        jpaRepository.findByIdAndUserId(id, userId)?.toDomain()

    override fun findByUserId(userId: Long): List<Customer> =
        jpaRepository.findByUserId(userId).map { it.toDomain() }

    override fun existsByDocumentAndUserId(document: String, userId: Long): Boolean =
        jpaRepository.existsByDocumentAndUserId(document, userId)

    override fun deleteByIdAndUserId(id: Long, userId: Long) =
        jpaRepository.deleteByIdAndUserId(id, userId)

    override fun countByUserIdAndCreatedAtBetween(userId: Long, start: LocalDateTime, end: LocalDateTime): Long =
        jpaRepository.countByUserIdAndCreatedAtBetween(userId, start, end)

    override fun countByUserId(userId: Long): Long =
        jpaRepository.countByUserId(userId)

    override fun findByUserIdWithFilters(userId: Long, search: String?, document: String?, phone: String?, pageable: Pageable): Page<Customer> {
        val page = jpaRepository.findByUserIdWithFilters(userId, search, document, phone, pageable)
        return PageImpl(page.content.map { it.toDomain() }, pageable, page.totalElements)
    }
}
