package com.test.unidadeshabitacionales

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences =
            this.getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE)
        val loggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (loggedIn) {
            val intent = Intent(this, HomeNavActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}