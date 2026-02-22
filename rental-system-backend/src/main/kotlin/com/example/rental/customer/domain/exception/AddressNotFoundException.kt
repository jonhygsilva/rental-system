package com.example.rental.customer.domain.exception

class AddressNotFoundException(val identifier: String) :
    RuntimeException("Address not found: $identifier")

