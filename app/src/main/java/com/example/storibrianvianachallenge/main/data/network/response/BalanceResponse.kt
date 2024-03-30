package com.example.storibrianvianachallenge.main.data.network.response

import com.google.gson.annotations.SerializedName

data class BalanceResponse(
    @SerializedName("fecha") var fecha: String? = null,
    @SerializedName("saldo_monto") var saldoMonto: Double? = null
)