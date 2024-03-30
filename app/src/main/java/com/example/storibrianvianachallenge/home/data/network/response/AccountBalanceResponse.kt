package com.example.storibrianvianachallenge.home.data.network.response

import com.google.gson.annotations.SerializedName

data class AccountBalanceResponse(
    @SerializedName("saldo") var saldo: BalanceResponse? = BalanceResponse()
)