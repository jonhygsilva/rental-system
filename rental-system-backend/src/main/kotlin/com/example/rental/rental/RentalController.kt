package com.example.rental.rental

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rentals")
class RentalController(private val service: RentalService) {

    @GetMapping
    fun all(@RequestParam userId: Long) = service.listByUser(userId)

    @PostMapping
    fun create(@RequestBody r: RentalRecordEntity) = service.create(r)
}
