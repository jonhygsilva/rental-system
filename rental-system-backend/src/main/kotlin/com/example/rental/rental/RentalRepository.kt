package com.example.rental.rental

import org.springframework.data.jpa.repository.JpaRepository

interface RentalRepository : JpaRepository<RentalRecordEntity, Long>
