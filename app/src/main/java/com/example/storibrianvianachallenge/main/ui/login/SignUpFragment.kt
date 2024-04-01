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
import com.example.storibrianvianachallenge.common.ui.dialog.ScannerFragment
import com.example.storibrianvianachallenge.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var scannedDataFront: String? = null
    private var scannedDataBack: String? = null
    private val loginViewModel by viewModels<LoginViewModel>()
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initUIState()
        initSignUp()
        initNavigation()
    }

    private fun initSignUp() {
        binding.apply {
            btnSignUp.setOnClickListener {
                val nombre = etNombre.text.toString().trim()
                val apellido = etApellido.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    showToast("Por favor, llene todos los campos.")
                } else if (scannedDataFront == null || scannedDataBack == null) {
                    showToast("Por favor, capture tanto la parte frontal como la trasera de su ID.")
                } else {
                    loginViewModel.signUpWithEmailAndPassword(email, password, nombre, apellido)
                    binding.pbar.isVisible = true
                }
            }
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginViewModel.state.collect {
                    when (it) {
                        is LoginState.Error -> errorSignUp(it.error)
                        LoginState.Loading -> loadingSignUo()
                        is LoginState.SuccessLogin -> succesSignUp(it.message)
                    }
                }
            }
        }
    }

    private fun loadingSignUo() {
        binding.pbar.isVisible = false
    }

    private fun succesSignUp(message: String?) {
        binding.pbar.isVisible = false
        findNavController().navigate(
            SignUpFragmentDirections.actionSignUpFragmentToOnSuccessFragment(
                message ?: "Usuario creado con éxito, espera a que se active tu cuenta"
            )
        )
    }

    private fun errorSignUp(message: String?) {
        binding.pbar.isVisible = false
        val fragmentManager = childFragmentManager
        val faiulureDialog = OnFailureDialog(
            message = message ?: "Ups, algo salio mal",
        )
        faiulureDialog.show(fragmentManager, "failure")
    }


    private fun scannerCameraID() {
        binding.apply {
            lnFront.setOnClickListener {
                val fragmentManager = childFragmentManager
                val scannerDialogFront = ScannerFragment(
                    type = "Captura de ID Frontal",
                )
                /*scannerDialogFront.setScannerResultListener { result ->
                    scannedDataFront = result
                }
                 */
                scannerDialogFront.show(fragmentManager, "scanner_front")
            }
            lnBack.setOnClickListener {
                val fragmentManager = childFragmentManager
                val scannerDialogBack = ScannerFragment(
                    type = "Captura de ID Trasera",
                )
                /*
                scannerDialogBack.setScannerResultListener { result ->
                    scannedDataBack = result
                }
                 */
                scannerDialogBack.show(fragmentManager, "scanner_back")
            }
        }
    }

    private fun initNavigation() {
        binding.apply {
            tvForgetLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}