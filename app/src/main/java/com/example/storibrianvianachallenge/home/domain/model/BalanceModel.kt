package com.example.storibrianvianachallenge.home.domain.model

import com.example.storibrianvianachallenge.home.data.network.response.BalanceResponse

data class BalanceModel(
    var fecha: String? = null,
    var saldoMonto: Double? = null
)

fun BalanceResponse.toDomain() = BalanceModel(fecha, saldoMonto)