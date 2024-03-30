package com.example.storibrianvianachallenge.main.data.network

import com.example.storibrianvianachallenge.main.data.network.response.AccountBalanceResponse
import com.example.storibrianvianachallenge.main.data.network.response.MovementsResponse
import com.example.storibrianvianachallenge.main.data.network.response.TransactionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BankInformationApiClient {

    @GET("movimientos/{id}.json")
    suspend fun getAllMovements(@Path("id") id: String): Response<MovementsResponse>

    @GET("consulta-movimientos/{id}/transaccion.json")
    suspend fun getMovement(@Path("id") id: Int): Response<TransactionResponse>

    @GET("saldo/{id}.json")
    suspend fun getBalance(@Path("id") id: String): Response<AccountBalanceResponse>
}