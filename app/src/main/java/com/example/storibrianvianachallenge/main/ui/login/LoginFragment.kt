package com.example.storibrianvianachallenge.main.ui.login

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.common.ui.dialog.OnFailureDialog
import com.example.storibrianvianachallenge.databinding.FragmentLoginBinding
import com.google.android.material.textfield.TextInputEditText
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
        forgotPassword()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.state.collect {
                    when (it) {
                        is LoginState.Error -> errorLogin(it.error)
                        LoginState.Loading -> loadingLogin()
                        is LoginState.SuccessLogin -> succesLogin(it.message)
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
                showToast()
            } else {
                loginViewModel.signInWithEmailAndPassword(email, password)
            }
        }
    }

    private fun initUIForget() {
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                loginViewModel.stateForget.collect{
                    when(it){
                        is LoginState.Error -> errorLogin(it.error)
                        LoginState.Loading -> loadingLogin()
                        is LoginState.SuccessLogin -> succesForget(it.message)
                    }
                }
            }
        }
    }
    private fun forgotPassword() {
        binding.tvForget.setOnClickListener {
            initUIForget()
            val editText =  TextInputEditText(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                )
                maxLines = 1
                filters = arrayOf<InputFilter>(InputFilter.LengthFilter(40))
                hint = "Correo@electronico.com"
            }
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Restablecer contraseña")
                .setMessage("Por favor, introduce tu correo electrónico:")
                .setView(editText)
                .setPositiveButton("Enviar") { dialog, _ ->
                    val email = editText.text.toString().trim()
                    if (email.isNotEmpty()) {
                        loginViewModel.sendPasswordResetEmail(email)
                    } else {
                        showToast()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialog.show()
        }
    }

    private fun initNavigation() {
        binding.apply {
            btnSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }
    }

    private fun loadingLogin() {
        binding.pbar.isVisible = false
    }

    private fun succesLogin(userId: String?) {
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

    private fun succesForget(message: String?) {
        binding.pbar.isVisible = false
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToOnSuccessFragment(
                message ?: "Usuario creado con éxito, espera a que se active tu cuenta"
            )
        )
    }

    private fun showToast() {
        Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
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