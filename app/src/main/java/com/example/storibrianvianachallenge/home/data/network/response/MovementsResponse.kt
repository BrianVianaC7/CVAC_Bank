package com.example.storibrianvianachallenge.home.data.network.response

import com.google.gson.annotations.SerializedName

data class MovementsResponse(
    @SerializedName("movimientos") var movimientos: ArrayList<MovementResponse> = arrayListOf()
)