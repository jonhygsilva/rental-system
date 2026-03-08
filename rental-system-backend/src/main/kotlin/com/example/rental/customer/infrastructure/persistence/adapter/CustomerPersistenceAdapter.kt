package com.example.rental.customer.infrastructure.persistence.adapter

import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.customer.domain.mapper.toDomain
import com.example.rental.customer.domain.mapper.toJpaEntity
import com.example.rental.customer.domain.model.Customer
import com.example.rental.customer.infrastructure.persistence.entity.CustomerJpaEntity
import com.example.rental.customer.infrastructure.persistence.repository.JpaCustomerRepository
import jakarta.persistence.criteria.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
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

    override fun findByUserIdWithFilters(
        userId: Long,
        search: String?,
        document: String?,
        phone: String?,
        pageable: Pageable
    ): Page<Customer> {

        val spec = Specification<CustomerJpaEntity> { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            // 1. Filtro obrigatório por UserId
            predicates.add(cb.equal(root.get<Long>("userId"), userId))

            // 2. Filtro de busca global (OR)
            search?.takeIf { it.isNotBlank() }?.let {
                val searchLower = "%${it.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("name")), searchLower),
                        cb.like(cb.lower(root.get("document")), searchLower),
                        cb.like(cb.lower(root.get("phone")), searchLower)
                    )
                )
            }

            // 3. Filtros específicos (AND)
            document?.takeIf { it.isNotBlank() }?.let {
                predicates.add(cb.equal(root.get<String>("document"), it))
            }
            phone?.takeIf { it.isNotBlank() }?.let {
                predicates.add(cb.equal(root.get<String>("phone"), it))
            }

            cb.and(*predicates.toTypedArray())
        }

        // Chama o findAll passando a Specification
        val page = jpaRepository.findAll(spec, pageable)

        return page.map { it.toDomain() } // O Spring Data já tem o .map direto na Page!
    }
}
