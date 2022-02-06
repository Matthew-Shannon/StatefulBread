package com.matthew.statefulbread.view.auth.frags

import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.core.hideKeyboard
import com.matthew.statefulbread.databinding.LoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : BaseFragment<LoginBinding>(LoginBinding::inflate) {

    override fun onResume() {
        super.onResume()
        binding.registerButton.setOnClickListener { navService.toRegister(R.id.splash_container) }
        binding.loginButton.setOnClickListener { onSubmit() }

    }

    private fun onSubmit() {
        activity?.hideKeyboard(binding.root)
        val email = binding.emailEditText.text?.trim()?.toString() ?: ""
        val password = binding.passwordEditText.text?.trim()?.toString() ?: ""

        if (email.isEmpty()) { binding.emailEditText.error = "Blank Email Address"; return }
        if (!email.contains("@")) { binding.emailEditText.error = "Invalid Email Address"; return }
        if (password.isEmpty()) { binding.passwordEditText.error = "Blank Password"; return }
        if (email != prefsService.getString("email")) { binding.emailEditText.error = "Incorrect Credentials"; return }

        prefsService.setString("password", password)
        navService.toMain()
    }

}

