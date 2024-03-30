package com.example.storibrianvianachallenge.home.domain.model

import com.example.storibrianvianachallenge.home.data.network.response.MovementResponse
import com.example.storibrianvianachallenge.home.data.network.response.TransactionResponse

data class TransactionModel(
    var detalle: String? = null,
    var fecha: String? = null,
    var idTransaccion: String? = null,
    var importe: Double? = null,
    var iva: Double? = null,
    var montoTotal: Double? = null,
    var referencia: String? = null,
    var tipo: String? = null,
    //SE AGREGA UNICAMENTE PARA EL MOVEMENTRESPONSE
    var idMovimiento: Int? = null
)

fun TransactionResponse.toDomain() = TransactionModel(detalle, fecha, idTransaccion, importe, iva, montoTotal, referencia, tipo)

fun MovementResponse.toDomain() = TransactionModel(
    detalle = this.detalle,
    fecha = this.fecha,
    idMovimiento = this.idmovimiento,
    montoTotal = this.montoTotal,
    referencia = this.numeroReferencia
)