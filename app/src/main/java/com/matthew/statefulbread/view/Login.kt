package com.matthew.statefulbread.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.matthew.statefulbread.App
import com.matthew.statefulbread.TAG
import com.matthew.statefulbread.databinding.ActivityLoginBinding
import com.matthew.statefulbread.hideKeyboard
import com.matthew.statefulbread.service.IPrefs

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val prefs: IPrefs by lazy { App.castToApp(this).prefs }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        Log.d(TAG, "onCreate")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        binding.registerButton.setOnClickListener { navToRegister() }
        binding.loginButton.setOnClickListener { onSubmit() }
    }

    private fun navToHome() = startActivity(Intent(this, Home::class.java))

    private fun navToRegister() = startActivity(Intent(this, Register::class.java))

    private fun onSubmit() {
        hideKeyboard(binding.root)
        val email = binding.emailEditText.text?.trim()?.toString() ?: ""
        val password = binding.passwordEditText.text?.trim()?.toString() ?: ""

        if (email.isEmpty()) { binding.emailEditText.error = "Blank Email Address"; return }
        if (!email.contains("@")) { binding.emailEditText.error = "Invalid Email Address"; return }
        if (password.isEmpty()) { binding.passwordEditText.error = "Blank Password"; return }
        if (email != prefs.getString("email") || password != prefs.getString("password")) { binding.emailEditText.error = "Incorrect Credentials"; return }
        navToHome()
    }

}

