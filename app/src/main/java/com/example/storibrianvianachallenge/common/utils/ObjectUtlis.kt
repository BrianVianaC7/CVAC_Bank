package com.example.storibrianvianachallenge.common.utils

import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object ObjectUtlis {
    fun String.maskAsteriskNumber(): String {
        return if (this.length > 4) {
            val maskedNumber = this.substring(0, this.length - 4).replace("[0-9]".toRegex(), "*")
            maskedNumber + this.substring(this.length - 4, this.length)
        } else {
            this
        }
    }

    fun Double.formatAsCurrency(): String {
        return String.format("$ %.2f", this)
    }

    fun formatDateTimeTo12Hour(dateTimeString: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(dateTimeString)
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return date?.let { outputFormat.format(it) }
    }

    fun formatTimestampToDashesDate(timestamp: Int): String {
        val sdf = SimpleDateFormat("dd–MM–yyyy", Locale.getDefault())
        val date = Date(timestamp.toLong() * 1000)
        return sdf.format(date)
    }

    fun formatTimestampToLongDate(timestamp: Int): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = Date(timestamp.toLong() * 1000)
        return sdf.format(date)
    }

}