package com.example.rental.rental.application.port.input

import com.example.rental.rental.application.command.CreateRentalCommand
import com.example.rental.rental.domain.model.RentalStatus
import com.example.rental.rental.web.dto.RentalResponse

interface UpdateRentalInput {
    fun update(id: Long, command: CreateRentalCommand): RentalResponse
    fun updateStatus(userId: Long, id: Long, status: RentalStatus): RentalResponse
}
