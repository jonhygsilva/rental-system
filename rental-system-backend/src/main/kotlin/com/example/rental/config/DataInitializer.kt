package com.example.rental.config

import com.example.rental.customer.domain.mapper.toJpaEntity
import com.example.rental.customer.domain.model.Address
import com.example.rental.customer.domain.model.Customer
import com.example.rental.customer.infrastructure.persistence.entity.AddressJpaEntity
import com.example.rental.customer.infrastructure.persistence.entity.CustomerJpaEntity
import com.example.rental.customer.infrastructure.persistence.repository.JpaCustomerRepository
import com.example.rental.equipment.infrastructure.persistence.entity.EquipmentJpaEntity
import com.example.rental.equipment.infrastructure.persistence.entity.EquipmentJpaStatus
import com.example.rental.equipment.infrastructure.persistence.repository.JpaEquipmentRepository
import com.example.rental.rental.RentalEntity
import com.example.rental.rental.RentalRepository
import com.example.rental.user.infrastructure.persistence.entity.UserJpaEntity
import com.example.rental.user.infrastructure.persistence.repository.JpaUserRepository
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
        rentalRepo: RentalRepository
    ) = CommandLineRunner {
        val user = userRepo.save(
            UserJpaEntity(
                name = "João Silva",
                email = "joao@example.com",
                password = passwordEncoder.encode("123456")
            )
        )

        // keep two initial customers as examples
        val customers = mutableListOf<CustomerJpaEntity>()

        val c1 = Customer(
            name = "Construtora Alfa",
            document = "12.345.678/0001-99",
            phone = "(11)99999-1111",
            userId = user.id,
            createdAt = LocalDate.now(),
            addresses = mutableListOf(
                Address(
                    street = "Avenida Brasil",
                    number = "500",
                    city = "São Paulo",
                    state = "SP",
                    zipCode = "01000-000",
                    complement = null,
                    neighborhood = "Centro"
                )
            )
        )

        customers += clienteRepo.save(c1.toJpaEntity())

        val c2 = Customer(
            name = "Lojas Gamma",
            document = "87.654.321/0001-44",
            phone = "(11)97777-3333",
            userId = user.id,
            createdAt = LocalDate.now(),
            addresses = mutableListOf(
                Address(
                    street = "Rua das Palmeiras",
                    number = "200",
                    city = "São Paulo",
                    state = "SP",
                    zipCode = "01002-000",
                    complement = null,
                    neighborhood = "Jardins"
                )
            )
        )

        customers += clienteRepo.save(c2.toJpaEntity())

        // generate ~18 additional customers programmatically (total ~20)
        val names = listOf(
            "Ace Construções", "Beta Obras", "Delta Serviços", "Epsilon Engenharia",
            "Fenix Empreendimentos", "Gama Reforma", "Icaro Instalações", "Kilo Locadora",
            "Lambda Materiais", "Mira Equipamentos", "Nexus Construções", "Omega Aluguel",
            "Polo Ferramentas", "Quasar Serviços", "Ridge Trade", "Sigma Máquinas",
            "Taurus Construções", "Urbis Projetos"
        )

        var docCounter = 100000000001L
        names.forEachIndexed { idx, n ->
            val cust = Customer(
                name = n,
                document = "%02d.%03d.%03d/0001-%02d".format((docCounter / 1000000000) % 100, (docCounter / 1000000) % 1000, (docCounter / 1000) % 1000, idx % 99),
                phone = "(11)9%05d-%04d".format(70000 + idx, 1000 + idx),
                userId = user.id,
                createdAt = LocalDate.now().minusDays((idx * 2).toLong()),
                addresses = mutableListOf(
                    Address(
                        street = "Rua ${idx + 10}",
                        number = "${100 + idx}",
                        city = "São Paulo",
                        state = "SP",
                        zipCode = "010%03d-000".format(idx),
                        complement = null,
                        neighborhood = "Bairro ${idx + 1}"
                    )
                )
            )
            customers += clienteRepo.save(cust.toJpaEntity())
            docCounter += 1
        }

        // create equipments: keep two initial ones then generate more
        val equipments = mutableListOf<EquipmentJpaEntity>()

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
        equipments += e1

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
        equipments += e2

        // generate additional equipments
        val equipmentNames = listOf("Compactador", "Plataforma Elevatória", "Martelete", "Serra Circular", "Escavadeira", "Mini-Pá", "Betoneira 200L", "Andaime", "Guincho", "Cortadora de Piso")
        equipmentNames.forEachIndexed { i, en ->
            val eq = EquipmentJpaEntity(
                name = en,
                type = if (i % 2 == 0) "Construção" else "Ferramenta",
                status = if (i % 3 == 0) EquipmentJpaStatus.MANUTENCAO else EquipmentJpaStatus.DISPONIVEL,
                latitude = -23.55 - i * 0.001,
                longitude = -46.63 + i * 0.001,
                dailyRate = BigDecimal(80 + i * 5),
                userId = user.id
            )
            equipments += equipamentoRepo.save(eq)
        }

        // create rentals linking some customers and equipments
        val today = LocalDate.now()
        val rentals = mutableListOf<RentalEntity>()

        // create ~20 rentals; reuse customers and equipments randomly
        val totalToCreate = 20
        for (i in 0 until totalToCreate) {
            val custEntity = customers[i % customers.size]
            val equipmentEntity = equipments[i % equipments.size]
            val addressEntity = if (custEntity.addresses.isNotEmpty()) custEntity.addresses[0] else AddressJpaEntity()

            val start = today.minusDays((i % 10).toLong())
            val end = start.plusDays(1 + (i % 7).toLong())
            val days = java.time.temporal.ChronoUnit.DAYS.between(start, end).toInt()
            val dailyRate = equipmentEntity.dailyRate ?: BigDecimal("100.00")
            val total = dailyRate.multiply(BigDecimal.valueOf(days.toLong()))

            val rental = RentalEntity(
                customer = custEntity,
                equipment = equipmentEntity,
                address = addressEntity,
                userId = user.id,
                startDate = start,
                endDate = end,
                total = total,
                paid = i % 2 == 0,
                status = com.example.rental.rental.domain.model.RentalStatus.ACTIVE
            )
            rentals += rentalRepo.save(rental)
        }

        println("✅ Seed data created - user joao@example.com / 123456")
        println("Created ${customers.size} customers, ${equipments.size} equipments and ${rentals.size} rentals")
    }
}
