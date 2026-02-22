package com.example.rental.customer.adapter.outbound.persistence.persistence

import com.example.rental.customer.adapter.outbound.persistence.mapper.toDomain
import com.example.rental.customer.adapter.outbound.persistence.mapper.toJpaEntity
import com.example.rental.customer.adapter.outbound.persistence.repository.JpaAddressRepository
import com.example.rental.customer.application.port.output.AddressPersistencePort
import com.example.rental.customer.domain.model.Address
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AddressPersistenceAdapter(
    private val jpaRepository: JpaAddressRepository
) : AddressPersistencePort {

    private val log = LoggerFactory.getLogger(AddressPersistenceAdapter::class.java)

    override fun save(address: Address): Address {
        log.debug("Persisting address for customer: {}", address.customerId)
        return jpaRepository.save(address.toJpaEntity()).toDomain()
    }

    override fun findById(id: Long): Address? {
        log.debug("Finding address by id: {}", id)
        return jpaRepository.findById(id).orElse(null)?.toDomain()
    }

    override fun findByCustomerId(customerId: Long): List<Address> {
        log.debug("Finding addresses by customerId: {}", customerId)
        return jpaRepository.findByCustomerId(customerId).map { it.toDomain() }
    }

    override fun deleteById(id: Long) {
        log.debug("Deleting address by id: {}", id)
        jpaRepository.deleteById(id)
    }
}