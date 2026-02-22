package com.example.rental.customer.adapter.outbound.persistence.repository

import com.example.rental.customer.adapter.outbound.persistence.entities.AddressJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring Data JPA repository for addresses.
 */
interface JpaAddressRepository : JpaRepository<AddressJpaEntity, Long> {
    fun findByCustomerId(customerId: Long): List<AddressJpaEntity>
}