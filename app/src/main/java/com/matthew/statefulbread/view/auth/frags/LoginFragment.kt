package com.matthew.statefulbread.view.auth.frags

import com.jakewharton.rxbinding4.view.clicks
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.SplashNav
import com.matthew.statefulbread.databinding.LoginBinding
import com.matthew.statefulbread.service.IPrefs
import com.matthew.statefulbread.service.IStorage
import com.matthew.statefulbread.service.model.User
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginBinding>(LoginBinding::inflate) {

    @Inject lateinit var loginVM: LoginVM

    override fun onResume() {
        super.onResume()

        binding.registerButton.clicks()
            .flatMapCompletable { loginVM.navToRegister() }
            .subscribe().addTo(disposable)

        binding.loginButton.clicks()
            .doOnNext { onSubmit(getData()) }
            .subscribe().addTo(disposable)
    }

    fun onSubmit(data: Map<String,String>) = loginVM
        .onAttempt(data)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(this::onError)
        .addTo(disposable)

    fun onError(ex: Throwable) = when (ex) {
        is EmailBlankEx, is EmailInvalidEx, is EmailExistsEx -> binding.emailEditText.error = ex.message
        is PasswordBlankEx, is IncorrectCredentialsEx -> binding.passwordEditText.error = ex.message
        else -> {}
    }

    fun getData(): Map<String,String>  = mapOf(
        "email" to binding.emailEditText.text!!.trim().toString(),
        "password" to binding.passwordEditText.text!!.trim().toString()
    )

}

class LoginVM @Inject constructor(private val prefs: IPrefs, private val storage: IStorage, @SplashNav private val nav: INav) {

    fun navToRegister(): Completable = Completable.defer(nav::toRegister)

    fun onAttempt(req: Map<String,String>): Completable = Single
        .just(User.def(req))
        .flatMap(::onValidate)
        .flatMapCompletable(::onSubmit)

    fun onValidate(user: User): Single<User> = when {
        user.email.isEmpty() -> Single.error(EmailBlankEx())
        !user.email.contains("@") -> Single.error(EmailInvalidEx())
        user.password.isEmpty() -> Single.error(PasswordBlankEx())
        else -> Single.just(user)
    }

    fun onSubmit(user: User): Completable = storage.userRepo()
        .flatMapCompletable { userRepo -> userRepo
            .findByCredentials(user.email, user.password)
            .filter { it != User.empty() }.isEmpty
            .flatMap { if (it) Single.error(IncorrectCredentialsEx()) else Single.just(it) }
            .flatMapCompletable {
                Completable.mergeArray(
                    Completable.defer { prefs.setOwnerEmail(user.email) },
                    Completable.defer { prefs.setAuthStatus(true) },
                    Completable.defer(nav::toMain)
                )
            }
        }

}