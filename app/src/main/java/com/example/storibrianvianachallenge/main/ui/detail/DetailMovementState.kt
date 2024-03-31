package com.example.storibrianvianachallenge.main.ui.detail

sealed class DetailMovementState {

    data object Loading : DetailMovementState()

    data class Error(val error: String) : DetailMovementState()

    data class SuccessMovement(
        var detalle: String? = null,
        var fecha: Int? = null,
        var idTransaccion: String? = null,
        var importe: Double? = null,
        var iva: Double? = null,
        var montoTotal: Double? = null,
        var referencia: String? = null,
        var tipo: String? = null,
    ) : DetailMovementState()
}
