package com.example.storibrianvianachallenge.main.ui.login


import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val database = Firebase.database.reference
    private var _state = MutableStateFlow<LoginState>(LoginState.Loading)
    val state: StateFlow<LoginState> get() = _state

    fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        nombre: String,
        apellido: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid ?: ""

                    val userRef = database.child("users").child(userId)
                    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                _state.value = LoginState.Error("Ya existe un usuario con este ID")
                            } else {
                                val userData = hashMapOf(
                                    "nombre" to nombre,
                                    "apellido" to apellido,
                                    "correo" to email
                                )
                                userRef.setValue(userData)
                                _state.value = LoginState.SuccessLogin("Usuario creado con éxito")
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            _state.value =
                                LoginState.Error("Error al crear el usuario: ${error.message}")
                        }
                    })
                } else {
                    _state.value = LoginState.Error("Ocurrió un problema al crear la cuenta")
                }
            }
    }


}