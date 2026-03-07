package com.example.rental.user.domain.exception

class UserAlreadyExistsException(val email: String) :
    RuntimeException("User already exists with email: $email")
