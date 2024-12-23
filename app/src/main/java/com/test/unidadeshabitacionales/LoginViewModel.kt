package com.test.unidadeshabitacionales

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

     suspend fun login(user: String, pass: String) {
        val result = FirebaseAuthManager.instance.logIn(email = user, password = pass)
        result.onSuccess {
            _loginStatus.value = true
        }.onFailure { error ->
            println("Error en el login: ${error.message}")
            _loginStatus.value = false
        }
    }
}
