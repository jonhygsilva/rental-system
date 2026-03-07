package com.example.rental.rental.application.port.input

import com.example.rental.rental.domain.model.RentalStatus

interface GetRentalStatsInput {
    fun getStats(userId: Long): Map<RentalStatus, Long>
}
