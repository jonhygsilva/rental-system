package com.example.rental.customer.infrastructure.persistence.repository

import com.example.rental.customer.infrastructure.persistence.entity.AddressJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository for addresses.
 */
interface JpaAddressRepository : JpaRepository<AddressJpaEntity, Long> {
    fun findByCustomerId(customerId: Long): List<AddressJpaEntity>
}
