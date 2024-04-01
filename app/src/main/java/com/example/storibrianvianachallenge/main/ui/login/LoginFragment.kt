package com.example.storibrianvianachallenge.main.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val loginViewModel by viewModels<LoginViewModel>()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initUIState()
        initNavigation()
        forgotPassword()
    }

    private fun forgotPassword() {
        TODO("Not yet implemented")
    }

    private fun initUIState() {
        TODO("Not yet implemented")
    }

    private fun initNavigation() {
        binding.apply {
            btnSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}