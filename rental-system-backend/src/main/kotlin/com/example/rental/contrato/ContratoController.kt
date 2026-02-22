package com.example.rental.contrato

import com.example.rental.contrato.domain.ContratoEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/contratos")
class ContratoController(private val service: ContratoService) {

    @GetMapping
    fun all(@RequestParam userId: Long) = service.listByUser(userId)

    @PostMapping
    fun create(@RequestBody c: ContratoEntity) = service.create(c)
}
