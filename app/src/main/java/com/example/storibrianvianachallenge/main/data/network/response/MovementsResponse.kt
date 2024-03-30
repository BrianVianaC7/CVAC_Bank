package com.example.storibrianvianachallenge.main.data.network.response

import com.google.gson.annotations.SerializedName

data class MovementsResponse(
    @SerializedName("movimientos") var movimientos: ArrayList<MovementResponse> = arrayListOf()
)