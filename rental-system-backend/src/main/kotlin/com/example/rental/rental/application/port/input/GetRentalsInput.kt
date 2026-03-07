package com.example.rental.rental.application.port.input

import com.example.rental.rental.web.dto.RentalResponse

interface GetRentalsInput {
    fun getRental(userId: Long, id: Long): RentalResponse
    fun getRentals(userId: Long): List<RentalResponse>
}
