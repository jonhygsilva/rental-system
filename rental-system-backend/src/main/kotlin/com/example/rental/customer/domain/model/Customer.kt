package com.example.rental.customer.domain.model

import com.example.rental.customer.domain.exception.DuplicateDocumentException
import com.example.rental.customer.domain.mapper.toDomain
import com.example.rental.customer.application.command.CreateCustomerCommand
import com.example.rental.customer.application.usecase.validations.CustomerDocumentUniquenessChecker

data class Customer(
    val id: Long? = null,
    var name: String,
    var document: String,
    var phone: String,
    var userId: Long,
    val addresses: MutableList<Address> = mutableListOf()
) {

    init {
        require(name.isNotBlank()) { "name must not be blank" }
        require(document.isNotBlank()) { "document must not be blank" }
        require(phone.isNotBlank()) { "phone must not be blank" }
        require(userId > 0) { "userId must be greater than 0" }
    }

    fun updateFrom(src: CreateCustomerCommand) {
        document = src.document
        name = src.name
        phone = src.phone
        userId = src.userId

        addresses.clear()
        addresses.addAll(src.addresses.map { it.toDomain() })
    }

    fun changeDocument(newDocument: String, checker: CustomerDocumentUniquenessChecker) {
        if (this.document == newDocument) return
        if (!checker.isUnique(newDocument)) throw DuplicateDocumentException(newDocument)
        this.document = newDocument
    }

    fun documentAlreadyExists(checker: CustomerDocumentUniquenessChecker) {
        if (!checker.isUnique(document)) throw DuplicateDocumentException(document)
    }
}

