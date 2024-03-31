package com.example.storibrianvianachallenge.main.data.network.response

import com.google.gson.annotations.SerializedName

data class MovementResponse(
    @SerializedName("detalle") var detalle: String? = null,
    @SerializedName("fecha") var fecha: Int? = null,
    @SerializedName("fecha_hora") var fechaHora: String? = null,
    @SerializedName("idmovimiento") var idmovimiento: Int? = null,
    @SerializedName("monto_total") var montoTotal: Double? = null,
    @SerializedName("numero_referencia") var numeroReferencia: String? = null
)