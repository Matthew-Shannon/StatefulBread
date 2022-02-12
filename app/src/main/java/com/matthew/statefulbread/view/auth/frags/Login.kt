package com.matthew.statefulbread.view.auth.frags

import com.jakewharton.rxbinding4.view.clicks
import com.matthew.statefulbread.core.getValue
import com.matthew.statefulbread.core.hideKeyboard
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.databinding.LoginBinding
import com.matthew.statefulbread.repo.INav
import com.matthew.statefulbread.repo.IPrefs
import com.matthew.statefulbread.repo.IStorage
import com.matthew.statefulbread.repo.SplashNav
import com.matthew.statefulbread.repo.model.User
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@AndroidEntryPoint
class Login : BaseFragment<LoginBinding>(LoginBinding::inflate) {

    @Inject lateinit var loginVM: LoginVM

    override fun onResume() {
        super.onResume()

        binding.registerButton.clicks()
            .flatMapCompletable { loginVM.navToRegister() }
            .subscribe().addTo(disposable)

        binding.loginButton.clicks()
            .doOnNext { onSubmit() }
            .subscribe().addTo(disposable)
    }

    private fun onSubmit() {
        hideKeyboard(binding.root)
        val email = binding.emailEditText.getValue()
        val password = binding.passwordEditText.getValue()

        if (email.isEmpty()) binding.emailEditText.error = "Blank Email"
        else if (!email.contains("@")) binding.emailEditText.error = "Invalid Email"
        else if (password.isEmpty()) binding.passwordEditText.error = "Blank Password"
        else loginVM
            .onSubmit(email, password)
            .doOnError { binding.emailEditText.error = "Incorrect Credentials" }
                .subscribe().addTo(disposable)
    }

}

class LoginVM @Inject constructor(private val prefs: IPrefs, private val storage: IStorage, @SplashNav private val nav: INav) {

    fun navToRegister(): Completable = Completable.fromAction(nav::toRegister)

    fun navToMain(): Completable = Completable.fromAction(nav::toMain)

    fun onSubmit(email: String, password: String): Completable {
        return storage.userRepo()
            .flatMap { it.findByCredentials(email, password).defaultIfEmpty(User.empty()) }
            .flatMap { if(it != User.empty()) Single.just(it) else Single.error(Exception()) }
            .flatMapCompletable { prefs.setOwnerID(it.id) }
            .andThen(prefs.setAuthStatus(true))
            .andThen(navToMain())
    }

}