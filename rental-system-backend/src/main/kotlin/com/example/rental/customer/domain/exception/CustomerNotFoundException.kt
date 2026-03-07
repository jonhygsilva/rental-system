package com.example.rental.customer.domain.exception

class CustomerNotFoundException(val identifier: String) :
    RuntimeException("Customer not found: $identifier")
