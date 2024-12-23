package com.test.unidadeshabitacionales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        // Encontrar las vistas con findViewById
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Configurar el botón de inicio de sesión
        btnLogin.setOnClickListener {
            val user = etEmail.text.toString()
            val pass = etPassword.text.toString()
            lifecycleScope.launch {
                viewModel.login(user, pass)
            }

        }

        // Observar el estado del login
        viewModel.loginStatus.observe(this, Observer { isLoggedIn ->
            if (isLoggedIn) {
                val sharedPreferences =
                    this.getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("email", etEmail.text.toString())
                editor.putBoolean("isLoggedIn", true)
                editor.apply()
                // Navegar a la siguiente actividad si el login es exitoso
                val intent = Intent(this, HomeNavActivity::class.java)
                startActivity(intent)
                finish()  // Opcional: cerrar la actividad de login después de la navegación
            }
        })
    }
}
