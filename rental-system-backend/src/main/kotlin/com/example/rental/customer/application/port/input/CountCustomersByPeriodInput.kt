package com.example.rental.customer.application.port.input

import java.time.LocalDateTime

interface CountCustomersByPeriodInput {
    fun count(userId: Long, start: LocalDateTime, end: LocalDateTime): Long
}
