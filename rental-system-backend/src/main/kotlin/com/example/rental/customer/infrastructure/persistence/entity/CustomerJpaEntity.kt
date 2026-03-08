package com.example.rental.customer.infrastructure.persistence.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "customers")
class CustomerJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var name: String = "",
    var document: String = "",
    var phone: String = "",
    var userId: Long = 0,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    @OneToMany(
        mappedBy = "customer",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val addresses: MutableList<AddressJpaEntity> = mutableListOf()
)
