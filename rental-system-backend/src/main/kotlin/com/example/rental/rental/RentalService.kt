package com.example.rental.rental

import org.springframework.stereotype.Service

@Service
class RentalService(private val repo: RentalRepository) {
    fun listByUser(userId: Long) = repo.findAll().filter { it.userId == userId }
    fun create(r: RentalRecordEntity) = repo.save(r)
}
