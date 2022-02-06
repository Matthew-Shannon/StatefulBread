package com.matthew.statefulbread.view.auth

import android.os.Bundle
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.LoginBinding
import com.matthew.statefulbread.core.hideKeyboard

class Login : BaseActivity<LoginBinding>(LoginBinding::inflate) {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
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
        finish()
    }

}

