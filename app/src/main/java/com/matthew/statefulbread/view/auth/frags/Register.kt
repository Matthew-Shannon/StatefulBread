package com.matthew.statefulbread.view.auth.frags

import com.jakewharton.rxbinding4.view.clicks
import com.matthew.statefulbread.core.getValue
import com.matthew.statefulbread.core.hideKeyboard
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.databinding.RegisterBinding
import com.matthew.statefulbread.repo.INav
import com.matthew.statefulbread.repo.IStorage
import com.matthew.statefulbread.repo.SplashNav
import com.matthew.statefulbread.repo.model.User
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@AndroidEntryPoint
class Register : BaseFragment<RegisterBinding>(RegisterBinding::inflate) {

    @Inject lateinit var registerVM: RegisterVM

    override fun onResume() {
        super.onResume()

        binding.backButton.clicks()
            .flatMapCompletable { registerVM.navToLogin() }
            .subscribe().addTo(disposable)

        binding.registerButton.clicks()
            .doOnNext { onSubmit() }
            .subscribe().addTo(disposable)
    }

    private fun onSubmit() {
        hideKeyboard(binding.root)
        val user = User.def(mapOf(
            "age" to "0",
            "name" to binding.nameEditText.getValue(),
            "email" to binding.emailEditText.getValue(),
            "zipCode" to binding.zipCodeEditText.getValue(),
            "password" to binding.passwordEditText.getValue()
        ))

        if (user.name.isEmpty()) binding.nameEditText.error = "Blank Name"
        else if (user.email.isEmpty()) binding.emailEditText.error = "Blank Email"
        else if (!user.email.contains("@")) binding.emailEditText.error = "Invalid Email"
        else if (user.zipCode.isEmpty()) binding.zipCodeEditText.error = "Blank ZipCode"
        else if (user.password.isEmpty()) binding.passwordEditText.error = "Blank Password"
        else registerVM
            .onSubmit(user)
            .doOnError { binding.emailEditText.error = "Email Already Exists" }
            .subscribe().addTo(disposable)
    }

}

class RegisterVM @Inject constructor(private val storage: IStorage, @SplashNav private val nav: INav) {

    fun navToLogin(): Completable = Completable.defer(nav::toLogin)

    fun onSubmit(user: User): Completable = storage.userRepo()
        .flatMapCompletable {
            Completable.mergeArray(
                Completable.defer { it.insert(user) },
                Completable.defer(nav::toLogin)
            )
        }

}