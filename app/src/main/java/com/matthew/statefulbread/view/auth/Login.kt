package com.matthew.statefulbread.view.auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.matthew.statefulbread.App
import com.matthew.statefulbread.TAG
import com.matthew.statefulbread.databinding.ActivityLoginBinding
import com.matthew.statefulbread.hideKeyboard
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.IPrefs

class Login : AppCompatActivity() {

    private val prefs: IPrefs by lazy { App.castToApp(this).prefs }
    private val nav: INav by lazy { App.castToApp(this).nav }
    private lateinit var binding: ActivityLoginBinding

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
        binding.registerButton.setOnClickListener { nav.toRegister(this) }
        binding.loginButton.setOnClickListener { onSubmit() }
    }

    private fun onSubmit() {
        hideKeyboard(binding.root)
        val email = binding.emailEditText.text?.trim()?.toString() ?: ""
        val password = binding.passwordEditText.text?.trim()?.toString() ?: ""

        if (email.isEmpty()) { binding.emailEditText.error = "Blank Email Address"; return }
        if (!email.contains("@")) { binding.emailEditText.error = "Invalid Email Address"; return }
        if (password.isEmpty()) { binding.passwordEditText.error = "Blank Password"; return }
        if (email != prefs.getString("email")) { binding.emailEditText.error = "Incorrect Credentials"; return }
        prefs.setString("password", password)
        nav.toHome(this)
    }

}

