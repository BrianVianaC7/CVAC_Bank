package com.example.storibrianvianachallenge.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.storibrianvianachallenge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.fragmentContainerView.isVisible = true
    }

    fun String.maskAsteriskNumber(): String {
        return if (this.length > 4) {
            val maskedNumber = this.substring(0, this.length - 4).replace("[0-9]".toRegex(), "*")
            maskedNumber + this.substring(this.length - 4, this.length)
        } else {
            this
        }
    }
}