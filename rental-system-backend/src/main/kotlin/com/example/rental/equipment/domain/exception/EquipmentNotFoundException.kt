package com.example.rental.equipment.domain.exception

class EquipmentNotFoundException(val identifier: String) :
    RuntimeException("Equipment not found: $identifier")

