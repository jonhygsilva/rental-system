package com.example.rental.customer.application.usecase.validations

import com.example.rental.customer.application.port.output.CustomerPersistencePort

class CustomerDocumentUniquenessCheckerImpl(
    private val persistencePort: CustomerPersistencePort
) : CustomerDocumentUniquenessChecker {
    override fun isUnique(document: String): Boolean =
        !persistencePort.existsByDocument(document)
}
