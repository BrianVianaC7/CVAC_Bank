package com.example.storibrianvianachallenge.main.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.common.ui.dialog.OnFailureDialog
import com.example.storibrianvianachallenge.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        initLogin()
        //forgotPassword()
    }

    private fun forgotPassword() {
        TODO("Not yet implemented")
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.state.collect {
                    when (it) {
                        is LoginState.Error -> errorLogin(it.error)
                        LoginState.Loading -> loadingSignUo()
                        is LoginState.SuccessLogin -> succesSignUp(it.message)
                    }
                }
            }
        }
    }

    private fun initLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                showToast("Por favor, completa todos los campos")
            } else {
                loginViewModel.signInWithEmailAndPassword(email, password)
            }
        }
    }

    private fun initNavigation() {
        binding.apply {
            btnSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
    }

    private fun loadingSignUo() {
        binding.pbar.isVisible = false
    }

    private fun succesSignUp(userId: String?) {
        binding.pbar.isVisible = false
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment(userId!!))
    }

    private fun errorLogin(message: String?) {
        binding.pbar.isVisible = false
        val fragmentManager = childFragmentManager
        val faiulureDialog = OnFailureDialog(
            message = message ?: "Ups, algo salio mal",
        )
        faiulureDialog.show(fragmentManager, "failure")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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