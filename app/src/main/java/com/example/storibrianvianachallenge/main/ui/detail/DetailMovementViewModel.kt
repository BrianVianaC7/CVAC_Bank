package com.example.storibrianvianachallenge.main.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storibrianvianachallenge.main.domain.model.TransactionModel
import com.example.storibrianvianachallenge.main.domain.usecase.GetMovementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailMovementViewModel @Inject constructor(
    private val getMovementUseCase: GetMovementUseCase
) : ViewModel() {

    private var _state = MutableStateFlow<DetailMovementState>(DetailMovementState.Loading)
    val state: StateFlow<DetailMovementState> get() = _state
    private var movementModel: TransactionModel? = TransactionModel()


    fun getMovement(id: Int) {
        movementModel?.idMovimiento = id
        viewModelScope.launch {
            _state.value = DetailMovementState.Loading
            val result = withContext(Dispatchers.IO) {
                getMovementUseCase(id)
            }
            if (result != null) {
                _state.value = DetailMovementState.SuccessMovement(
                    result.detalle,
                    result.fecha,
                    result.idTransaccion,
                    result.importe,
                    result.iva,
                    result.montoTotal,
                    result.referencia,
                    result.tipo
                )
            } else {
                _state.value = DetailMovementState.Error("Error al obtener el movimiento")
            }

        }
    }
}