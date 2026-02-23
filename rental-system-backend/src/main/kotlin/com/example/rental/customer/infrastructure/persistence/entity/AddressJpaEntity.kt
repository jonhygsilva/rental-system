package com.example.rental.customer.infrastructure.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "addresses")
class AddressJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var street: String = "",
    var number: String = "",
    var complement: String? = null,
    var neighborhood: String = "",
    var city: String = "",
    var state: String = "",
    var zipCode: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    var customer: CustomerJpaEntity? = null
)
