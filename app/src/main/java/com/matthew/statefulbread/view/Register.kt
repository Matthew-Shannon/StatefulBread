package com.matthew.statefulbread.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.matthew.statefulbread.App
import com.matthew.statefulbread.TAG
import com.matthew.statefulbread.databinding.ActivityRegisterBinding
import com.matthew.statefulbread.hideKeyboard
import com.matthew.statefulbread.service.IPrefs

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val prefs: IPrefs by lazy { App.castToApp(this).prefs }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        Log.d(TAG, "onCreate")
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        binding.backButton.setOnClickListener { navBack() }
        binding.registerButton.setOnClickListener { onSubmit() }
    }

    private fun navBack() = onBackPressed()

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
        prefs.setString("password", password)
        navBack()
    }

}