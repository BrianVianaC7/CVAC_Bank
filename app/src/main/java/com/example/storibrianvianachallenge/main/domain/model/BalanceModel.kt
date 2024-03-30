package com.example.storibrianvianachallenge.main.domain.model

import com.example.storibrianvianachallenge.main.data.network.response.BalanceResponse

data class BalanceModel(
    var fecha: String? = null,
    var saldoMonto: Double? = null
)

fun BalanceResponse.toDomain() = BalanceModel(fecha, saldoMonto)