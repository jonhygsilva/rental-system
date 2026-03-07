package com.example.rental.rental

import com.example.rental.customer.infrastructure.persistence.entity.AddressJpaEntity
import com.example.rental.customer.infrastructure.persistence.entity.CustomerJpaEntity
import com.example.rental.equipment.infrastructure.persistence.entity.EquipmentJpaEntity
import com.example.rental.rental.domain.model.RentalStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "rentals")
data class RentalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    val customer: CustomerJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    val equipment: EquipmentJpaEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    val address: AddressJpaEntity,
    val userId: Long,

    val startDate: LocalDate,
    val endDate: LocalDate,

    val total: BigDecimal,
    val paid: Boolean = false,

    @Enumerated(EnumType.STRING)
    var status: RentalStatus = RentalStatus.ACTIVE
)
