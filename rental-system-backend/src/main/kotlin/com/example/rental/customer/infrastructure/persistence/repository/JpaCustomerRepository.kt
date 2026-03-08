package com.example.rental.customer.infrastructure.persistence.repository

import com.example.rental.customer.infrastructure.persistence.entity.CustomerJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

/**
 * Spring Data JPA repository — infrastructure concern.
 */
interface JpaCustomerRepository : JpaRepository<CustomerJpaEntity, Long>, JpaSpecificationExecutor<CustomerJpaEntity> {
    @EntityGraph(attributePaths = ["addresses"])
    fun findByUserId(userId: Long): List<CustomerJpaEntity>
    fun findByIdAndUserId(id: Long, userId: Long): CustomerJpaEntity?
    fun deleteByIdAndUserId(id: Long, userId: Long)
    fun existsByDocument(document: String): Boolean
    fun existsByDocumentAndUserId(document: String, userId: Long): Boolean

    // count customers for a user created between two timestamps
    fun countByUserIdAndCreatedAtBetween(userId: Long, start: LocalDateTime, end: LocalDateTime): Long

    // count total customers for a user
    fun countByUserId(userId: Long): Long

    @Query(
        ""+
            "SELECT c FROM CustomerJpaEntity c " +
            "WHERE c.userId = :userId " +
            "AND (:search IS NULL OR (LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.document) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :search, '%')))) " +
            "AND (:document IS NULL OR c.document = :document) " +
            "AND (:phone IS NULL OR c.phone = :phone)"
    )
    fun findByUserIdWithFilters(userId: Long, search: String?, document: String?, phone: String?, pageable: Pageable): Page<CustomerJpaEntity>
}
