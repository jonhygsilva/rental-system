package com.example.rental.config

import com.example.rental.user.adapter.outbound.persistence.UserJpaEntity
import com.example.rental.user.adapter.outbound.persistence.JpaUserRepository
import com.example.rental.customer.infrastructure.persistence.entity.CustomerJpaEntity
import com.example.rental.customer.infrastructure.persistence.repository.JpaCustomerRepository
import com.example.rental.equipment.adapter.outbound.persistence.EquipmentJpaEntity
import com.example.rental.equipment.adapter.outbound.persistence.EquipmentJpaStatus
import com.example.rental.equipment.adapter.outbound.persistence.JpaEquipmentRepository
import com.example.rental.contrato.domain.ContratoEntity
import com.example.rental.contrato.ContratoRepository
import com.example.rental.rental.RentalRecordEntity
import com.example.rental.rental.RentalRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal
import java.time.LocalDate

@Configuration
class DataInitializer(private val passwordEncoder: PasswordEncoder) {

    @Bean
    fun init(
        userRepo: JpaUserRepository,
        clienteRepo: JpaCustomerRepository,
        equipamentoRepo: JpaEquipmentRepository,
        contratoRepo: ContratoRepository,
        rentalRepo: RentalRepository
    ) = CommandLineRunner {

        val user = userRepo.save(
            UserJpaEntity(
                name = "João Silva",
                email = "joao@example.com",
                password = passwordEncoder.encode("123456")
            )
        )

        val c1 = clienteRepo.save(
            CustomerJpaEntity(
                name = "Construtora Alfa",
                document = "12.345.678/0001-99",
                phone = "(11)99999-1111",
                userId = user.id
            )
        )

        val c2 = clienteRepo.save(
            CustomerJpaEntity(
                name = "Eventos Beta",
                document = "98.765.432/0001-55",
                phone = "(11)98888-2222",
                userId = user.id
            )
        )

        val e1 = equipamentoRepo.save(
            EquipmentJpaEntity(
                name = "Betoneira 400L",
                type = "Construção",
                status = EquipmentJpaStatus.LOCADO,
                latitude = -23.55052,
                longitude = -46.6333,
                dailyRate = BigDecimal("120.00"),
                userId = user.id
            )
        )

        val e2 = equipamentoRepo.save(
            EquipmentJpaEntity(
                name = "Gerador 5KVA",
                type = "Energia",
                status = EquipmentJpaStatus.DISPONIVEL,
                latitude = -23.5589,
                longitude = -46.6253,
                dailyRate = BigDecimal("150.00"),
                userId = user.id
            )
        )

        println("✅ Seed data created - user joao@example.com / 123456")
    }
}
