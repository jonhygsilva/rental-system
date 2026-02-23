package com.example.rental.customer.infrastructure.persistence.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "clientes")
class CustomerJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String = "",
    var document: String = "",
    var phone: String = "",
    var userId: Long = 0,

    @OneToMany(
        mappedBy = "customer",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val addresses: MutableList<AddressJpaEntity> = mutableListOf()
)
