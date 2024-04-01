package com.example.storibrianvianachallenge.main.ui.login

import com.example.storibrianvianachallenge.main.ui.detail.DetailMovementState


sealed class LoginState {
    data object Loading : LoginState()
    data class Error(val error: String) : LoginState()
    data class SuccessLogin (val message : String) : LoginState()
}