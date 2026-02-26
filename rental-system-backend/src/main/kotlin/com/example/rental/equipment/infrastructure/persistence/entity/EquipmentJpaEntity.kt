package com.example.rental.equipment.infrastructure.persistence.entity

import jakarta.persistence.*
import java.math.BigDecimal

/**
 * JPA entity — infrastructure concern only.
 * Maps to the "equipamentos" table.
 */
@Entity
@Table(name = "equipamentos")
data class EquipmentJpaEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String = "",
    val type: String = "",
    @Enumerated(EnumType.STRING)
    var status: EquipmentJpaStatus = EquipmentJpaStatus.DISPONIVEL,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val dailyRate: BigDecimal = BigDecimal.ZERO,
    val userId: Long = 0
)

/**
 * JPA-level enum — mirrors the domain enum for database mapping.
 */
enum class EquipmentJpaStatus {
    DISPONIVEL, LOCADO, MANUTENCAO
}
