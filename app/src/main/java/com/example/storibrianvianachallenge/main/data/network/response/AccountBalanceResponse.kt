package com.example.storibrianvianachallenge.main.data.network.response

import com.google.gson.annotations.SerializedName

data class AccountBalanceResponse(
    @SerializedName("saldo") var saldo: BalanceResponse? = BalanceResponse()
)