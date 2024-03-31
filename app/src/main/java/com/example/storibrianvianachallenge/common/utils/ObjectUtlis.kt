package com.example.storibrianvianachallenge.common.utils

object ObjectUtlis {
    fun String.maskAsteriskNumber(): String {
        return if (this.length > 4) {
            val maskedNumber = this.substring(0, this.length - 4).replace("[0-9]".toRegex(), "*")
            maskedNumber + this.substring(this.length - 4, this.length)
        } else {
            this
        }
    }
}