package com.example.storibrianvianachallenge.main.ui.home

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storibrianvianachallenge.common.ui.dialog.OnFailureDialog
import com.example.storibrianvianachallenge.main.domain.model.BalanceModel
import com.example.storibrianvianachallenge.main.domain.model.TransactionModel
import com.example.storibrianvianachallenge.main.domain.usecase.GetBalanceUseCase
import com.example.storibrianvianachallenge.main.domain.usecase.GetMovementsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovementsUseCase: GetMovementsUseCase,
    private val getBalanceUseCase: GetBalanceUseCase
) : ViewModel() {

    private val _movements = MutableStateFlow<List<TransactionModel>>(emptyList())
    val movements: StateFlow<List<TransactionModel>> get() = _movements
    private val _balance = MutableStateFlow(BalanceModel())
    val balance: StateFlow<BalanceModel> get() = _balance
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    fun getBalance(id: String, fragmentManager: FragmentManager) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val balanceFromApi = getBalanceUseCase(id)
                _balance.value = balanceFromApi
            } catch (e: Exception) {
                Log.e("BalanceError", "Error al obtener el saldo: ${e.message}")
                val failureFragment = OnFailureDialog("Error al obtener el saldo")
                failureFragment.show(fragmentManager, "failureFragment")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getMovements(id: String, fragmentManager: FragmentManager) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movementsFromApi = getMovementsUseCase(id)
                _movements.value = movementsFromApi
            } catch (e: Exception) {
                Log.e("MovementsError", "Error al obtener los movimientos: ${e.message}")
                val failureFragment = OnFailureDialog("Error al obtener los movimientos")
                failureFragment.show(fragmentManager, "failureFragment")
            } finally {
                _isLoading.value = false
            }
        }
    }


}