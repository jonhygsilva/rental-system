package com.example.rental.user.domain.exception

class UserNotFoundException(val identifier: String) :
    RuntimeException("User not found: $identifier")

