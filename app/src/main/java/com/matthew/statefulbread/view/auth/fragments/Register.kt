package com.matthew.statefulbread.view.auth.fragments

import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.BaseFragment
import com.matthew.statefulbread.core.hideKeyboard
import com.matthew.statefulbread.databinding.RegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : BaseFragment<RegisterBinding>(RegisterBinding::inflate) {

    override fun onResume() {
        super.onResume()
        binding.backButton.setOnClickListener { nav.toLogin(R.id.splash_container) }
        binding.registerButton.setOnClickListener { onSubmit() }
    }

    private fun onSubmit() {
        activity?.hideKeyboard(binding.root)
        val name = binding.nameEditText.text?.trim()?.toString() ?: ""
        val email = binding.emailEditText.text?.trim()?.toString() ?: ""
        val zipCode = binding.zipCodeEditText.text?.trim()?.toString() ?: ""
        val password = binding.passwordEditText.text?.trim()?.toString() ?: ""

        if (name.isEmpty()) { binding.nameEditText.error = "Blank Name"; return }
        if (email.isEmpty()) { binding.emailEditText.error = "Blank Email Address"; return }
        if (!email.contains("@")) { binding.emailEditText.error = "Invalid Email Address"; return }
        if (zipCode.isEmpty()) { binding.zipCodeEditText.error = "Blank Zip Code"; return }
        if (password.isEmpty()) { binding.passwordEditText.error = "Blank Password"; return }
        if (email == prefs.getString("email")) { binding.emailEditText.error = "Email Address Already Exists"; return }

        prefs.setString("name", name)
        prefs.setString("email", email)
        prefs.setString("zipCode", zipCode)
        nav.toLogin(R.id.splash_container)
    }

}