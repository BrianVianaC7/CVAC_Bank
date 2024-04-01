package com.example.storibrianvianachallenge.main.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.databinding.ActivityMainBinding
import com.example.storibrianvianachallenge.main.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.fragmentContainerView.isVisible = true
    }

    private fun logout() {
        loginViewModel.signOut()
    }

    override fun onDestroy() {
        super.onDestroy()
        logout()
    }
}