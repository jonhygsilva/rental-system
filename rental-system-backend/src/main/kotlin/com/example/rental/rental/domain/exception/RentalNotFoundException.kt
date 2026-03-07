package com.example.rental.rental.domain.exception

class RentalNotFoundException(identifier: String) :
    RuntimeException("Rental not found: $identifier")
