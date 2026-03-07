package com.example.rental.dashboard.web.dto

import com.example.rental.dashboard.domain.model.DashboardRecentRental
import java.math.BigDecimal

data class DashboardRecentRentalResponse(
    val id: Long,
    val customerId: Long,
    val customerName: String?,
    val equipmentId: Long,
    val equipmentName: String?,
    val startDate: String,
    val endDate: String,
    val total: BigDecimal,
    val status: String
) {
    companion object {
        fun from(domain: DashboardRecentRental) = DashboardRecentRentalResponse(
            id = domain.id,
            customerId = domain.customerId,
            customerName = domain.customerName,
            equipmentId = domain.equipmentId,
            equipmentName = domain.equipmentName,
            startDate = domain.startDate,
            endDate = domain.endDate,
            total = domain.total,
            status = domain.status
        )
    }
}
