package com.matthew.statefulbread.view.auth.frags

import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.hideKeyboard
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.databinding.RegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : BaseFragment<RegisterBinding>(RegisterBinding::inflate) {

    override fun onResume() {
        super.onResume()
        binding.backButton.setOnClickListener { navService.toLogin(R.id.splash_container) }
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
        if (email == prefsService.getEmail()) { binding.emailEditText.error = "Email Address Already Exists"; return }

        prefsService.setName(name)
        prefsService.setEmail(email)
        prefsService.setZipCode(zipCode)
        navService.toLogin(R.id.splash_container)
    }

}