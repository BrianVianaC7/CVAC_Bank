package com.example.storibrianvianachallenge.home.data.network

import com.example.storibrianvianachallenge.home.data.network.response.AccountBalanceResponse
import com.example.storibrianvianachallenge.home.data.network.response.BalanceResponse
import com.example.storibrianvianachallenge.home.data.network.response.MovementResponse
import com.example.storibrianvianachallenge.home.data.network.response.MovementsResponse
import com.example.storibrianvianachallenge.home.data.network.response.TransactionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class BankInformationService @Inject constructor(
    private val apiClient: BankInformationApiClient
) {
    suspend fun getAllMovements(id: String): List<MovementResponse> {
        return withContext(Dispatchers.IO) {
            val response: Response<MovementsResponse> = apiClient.getAllMovements(id)
            val mvResponse: MovementsResponse? = response.body()
            mvResponse?.movimientos ?: emptyList()
        }
    }

    suspend fun getMovement(id: Int): TransactionResponse {
        return withContext(Dispatchers.IO) {
            val response: Response<TransactionResponse> = apiClient.getMovement(id)
            response.body() ?: throw IllegalStateException("Movement not found for id: $id")
        }
    }

    suspend fun getBalance(id: String): BalanceResponse {
        return withContext(Dispatchers.IO) {
            val response: Response<AccountBalanceResponse> = apiClient.getBalance(id)
            val blResponse: AccountBalanceResponse? = response.body()
            blResponse?.saldo ?: throw IllegalStateException("Balance not found for id: $id")
        }
    }
}