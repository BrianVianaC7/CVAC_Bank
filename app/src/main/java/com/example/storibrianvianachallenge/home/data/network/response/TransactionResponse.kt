package com.example.storibrianvianachallenge.home.data.network.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("detalle") var detalle: String? = null,
    @SerializedName("fecha") var fecha: String? = null,
    @SerializedName("id_transaccion") var idTransaccion: String? = null,
    @SerializedName("importe") var importe: Double? = null,
    @SerializedName("iva") var iva: Double? = null,
    @SerializedName("monto_total") var montoTotal: Double? = null,
    @SerializedName("referencia") var referencia: String? = null,
    @SerializedName("tipo") var tipo: String? = null
)