package com.example.rental.customer.application.usecase.validations

interface CustomerDocumentUniquenessChecker {
    fun isUnique(document: String): Boolean
}
