package com.example.rental.customer.domain.exception

class DuplicateDocumentException(val document: String) :
    RuntimeException("Customer already exists with document: $document")
