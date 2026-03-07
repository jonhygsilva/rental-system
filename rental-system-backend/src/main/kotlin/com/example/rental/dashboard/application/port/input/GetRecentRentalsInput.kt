package com.example.rental.dashboard.application.port.input

import com.example.rental.dashboard.domain.model.DashboardRecentRental

interface GetRecentRentalsInput {
    fun getRecentRentals(userId: Long, size: Int): List<DashboardRecentRental>
}
