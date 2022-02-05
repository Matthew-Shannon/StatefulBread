package com.matthew.statefulbread.view.auth

import android.os.Bundle
import android.util.Log
import com.matthew.statefulbread.TAG
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.RegisterBinding
import com.matthew.statefulbread.hideKeyboard

class Register : BaseActivity<RegisterBinding>(RegisterBinding::inflate) {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        binding.backButton.setOnClickListener { nav.goBack(this) }
        binding.registerButton.setOnClickListener { onSubmit() }
    }

    private fun onSubmit() {
        hideKeyboard(binding.root)
        val name = binding.nameEditText.text?.trim()?.toString() ?: ""
        val email = binding.emailEditText.text?.trim()?.toString() ?: ""
        val zipCode = binding.zipCodeEditText.text?.trim()?.toString() ?: ""
        val password = binding.passwordEditText.text?.trim()?.toString() ?: ""
        Log.d(TAG, "email: $email password: $password")

        if (name.isEmpty()) { binding.nameEditText.error = "Blank Name"; return }
        if (email.isEmpty()) { binding.emailEditText.error = "Blank Email Address"; return }
        if (!email.contains("@")) { binding.emailEditText.error = "Invalid Email Address"; return }
        if (zipCode.isEmpty()) { binding.zipCodeEditText.error = "Blank Zip Code"; return }
        if (password.isEmpty()) { binding.passwordEditText.error = "Blank Password"; return }
        if (email == prefs.getString("email")) { binding.emailEditText.error = "Email Address Already Exists"; return }
        prefs.setString("name", name)
        prefs.setString("email", email)
        prefs.setString("zipCode", zipCode)
        nav.goBack(this)
    }

}