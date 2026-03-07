package com.example.rental.rental.application.port.input

import com.example.rental.rental.application.command.CreateRentalCommand
import com.example.rental.rental.web.dto.RentalResponse

interface CreateRentalInput {
    fun execute(command: CreateRentalCommand): RentalResponse
}
