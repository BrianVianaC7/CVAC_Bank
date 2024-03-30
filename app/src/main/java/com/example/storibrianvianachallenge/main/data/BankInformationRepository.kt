package com.example.storibrianvianachallenge.main.data

import com.example.storibrianvianachallenge.main.data.network.BankInformationService
import com.example.storibrianvianachallenge.main.data.network.response.BalanceResponse
import com.example.storibrianvianachallenge.main.data.network.response.MovementResponse
import com.example.storibrianvianachallenge.main.data.network.response.TransactionResponse
import com.example.storibrianvianachallenge.main.domain.model.BalanceModel
import com.example.storibrianvianachallenge.main.domain.model.TransactionModel
import com.example.storibrianvianachallenge.main.domain.model.toDomain
import javax.inject.Inject

class BankInformationRepository @Inject constructor(
    private val api: BankInformationService
) {
    suspend fun getAllMovements(id: String): List<TransactionModel> {
        val response: List<MovementResponse> = api.getAllMovements(id)
        return response.map { convert -> convert.toDomain() }
    }

    suspend fun getMovement(id: Int): TransactionModel {
        val response: TransactionResponse = api.getMovement(id)
        return response.toDomain()
    }

    suspend fun getBalance(id: String) : BalanceModel {
        val response: BalanceResponse = api.getBalance(id)
        return response.toDomain()
    }
}