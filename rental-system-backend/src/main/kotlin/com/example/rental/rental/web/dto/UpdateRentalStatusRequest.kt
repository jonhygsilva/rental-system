package com.example.rental.rental.web.dto

import com.example.rental.rental.domain.model.RentalStatus

data class UpdateRentalStatusRequest(
    val status: RentalStatus
)
