package com.example.rental.rental.application

import com.example.rental.customer.application.port.output.CustomerPersistencePort
import com.example.rental.customer.domain.exception.AddressNotFoundException
import com.example.rental.customer.domain.exception.CustomerNotFoundException
import com.example.rental.customer.domain.model.Customer
import com.example.rental.equipment.application.port.output.EquipmentPersistencePort
import com.example.rental.equipment.domain.exception.EquipmentNotFoundException
import com.example.rental.rental.application.command.CreateRentalCommand
import com.example.rental.rental.application.port.input.CreateRentalInput
import com.example.rental.rental.application.port.input.GetRentalStatsInput
import com.example.rental.rental.application.port.input.GetRentalsInput
import com.example.rental.rental.application.port.input.UpdateRentalInput
import com.example.rental.rental.application.port.output.RentalPersistencePort
import com.example.rental.rental.domain.exception.RentalNotFoundException
import com.example.rental.rental.domain.mapper.toDomain
import com.example.rental.rental.domain.model.RentalStatus
import com.example.rental.rental.web.dto.RentalResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RentalUseCase(
    private val rentalPersistencePort: RentalPersistencePort,
    private val customerPersistencePort: CustomerPersistencePort,
    private val equipmentPersistencePort: EquipmentPersistencePort
) : CreateRentalInput, GetRentalsInput, UpdateRentalInput, GetRentalStatsInput {

    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun execute(command: CreateRentalCommand): RentalResponse {
        val customer = getCustomerByIdAndUserId(command.customerId, command.userId)
        val equipment = getEquipmentById(command.equipmentId, command.userId)
        val address = getAddressFromCustomer(customer, command.addressId)

        val rental = command.toDomain(customer, equipment, address)
        val saved = rentalPersistencePort.save(rental)

        return RentalResponse.from(saved)
    }

    override fun getRentals(userId: Long): List<RentalResponse> {
        return rentalPersistencePort.findByUserId(userId).map { RentalResponse.from(it) }
    }

    override fun getRental(userId: Long, id: Long): RentalResponse {
        val rental = getRentalByIdAndUserId(id, userId)
        return RentalResponse.from(rental)
    }

    @Transactional
    override fun update(id: Long, command: CreateRentalCommand): RentalResponse {
        val customer = getCustomerByIdAndUserId(command.customerId, command.userId)
        val equipment = getEquipmentById(command.equipmentId, command.userId)
        val address = getAddressFromCustomer(customer, command.addressId)

        val rental = getRentalByIdAndUserId(id, command.userId)
        rental.updateFrom(command, customer, equipment, address)

        val updated = rentalPersistencePort.save(rental)
        return RentalResponse.from(updated)
    }

    @Transactional
    override fun updateStatus(userId: Long, id: Long, status: RentalStatus): RentalResponse {
        log.info("Updating equipment {} status to {}", id, status)
        val rental = getRentalByIdAndUserId(id, userId)

        val updated = rentalPersistencePort.save(rental.withStatus(status))
        log.info("Equipment {} status updated to {}", id, updated.status)
        return RentalResponse.from(updated)
    }

    @Transactional(readOnly = true)
    override fun getStats(userId: Long): Map<RentalStatus, Long> {
        return rentalPersistencePort.countByUserIdGroupByStatus(userId)
    }

    private fun getRentalByIdAndUserId(rentalId: Long, userId: Long) =
        rentalPersistencePort.findByIdAndUserId(rentalId, userId) ?: throw RentalNotFoundException(rentalId.toString())

    private fun getCustomerByIdAndUserId(customerId: Long, userId: Long) =
        customerPersistencePort.findByIdAndUserId(customerId, userId) ?: throw CustomerNotFoundException(customerId.toString())

    private fun getEquipmentById(equipmentId: Long, userId: Long) =
        equipmentPersistencePort.findByIdAndUserId(equipmentId, userId) ?: throw EquipmentNotFoundException(equipmentId.toString())

    private fun getAddressFromCustomer(customer: Customer, addressId: Long) =
        customer.addresses.firstOrNull { it.id == addressId }
            ?: throw AddressNotFoundException(addressId.toString())
}
