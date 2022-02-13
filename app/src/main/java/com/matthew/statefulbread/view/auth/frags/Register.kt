package com.matthew.statefulbread.view.auth.frags

import com.jakewharton.rxbinding4.view.clicks
import com.matthew.statefulbread.core.getValue
import com.matthew.statefulbread.core.view.BaseFragment
import com.matthew.statefulbread.core.view.INav
import com.matthew.statefulbread.core.view.SplashNav
import com.matthew.statefulbread.databinding.RegisterBinding
import com.matthew.statefulbread.repo.IStorage
import com.matthew.statefulbread.repo.model.User
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@AndroidEntryPoint
class Register : BaseFragment<RegisterBinding>(RegisterBinding::inflate) {

    @Inject lateinit var registerVM: RegisterVM

    override fun onResume() {
        super.onResume()

        binding.backButton.clicks()
            .concatMapCompletable { registerVM.navToLogin() }
            .subscribe().addTo(disposable)

        binding.registerButton.clicks()
            .doOnNext { onSubmit(getData()) }
            .subscribe().addTo(disposable)
    }

    private fun onSubmit(data: Map<String,String>) = registerVM
        .onAttempt(data)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(this::onError)
        .addTo(disposable)

    private fun onError(ex: Throwable) = when (ex) {
        is EmailBlankEx, is EmailInvalidEx, is EmailExistsEx -> binding.emailEditText.error = ex.message
        is NameBlankEx -> binding.nameEditText.error = ex.message
        is ZipCodeBlankEx -> binding.zipCodeEditText.error = ex.message
        is PasswordBlankEx -> binding.passwordEditText.error = ex.message
        else -> {}
    }

    private fun getData(): Map<String,String>  = mapOf(
        "age" to "0",
        "name" to binding.nameEditText.getValue(),
        "email" to binding.emailEditText.getValue(),
        "zipCode" to binding.zipCodeEditText.getValue(),
        "password" to binding.passwordEditText.getValue()
    )

}

class RegisterVM @Inject constructor(private val storage: IStorage, @SplashNav private val nav: INav) {

    fun navToLogin(): Completable = Completable.defer(nav::toLogin)

    fun onAttempt(req: Map<String,String>): Completable = Single
        .just(User.def(req))
        .flatMap(::onValidate)
        .flatMapCompletable(::onSubmit)

    fun onValidate(user: User): Single<User> = when {
        user.name.isEmpty() -> Single.error(NameBlankEx())
        user.email.isEmpty() -> Single.error(EmailBlankEx())
        !user.email.contains("@") -> Single.error(EmailInvalidEx())
        user.zipCode.isEmpty() -> Single.error(ZipCodeBlankEx())
        user.password.isEmpty() -> Single.error(PasswordBlankEx())
        else -> Single.just(user)
    }

    fun onSubmit(user: User): Completable = storage.userRepo()
        .flatMapCompletable { userRepo -> userRepo
            .findByCredentials(user.email, user.password)
            .filter { it != User.empty() }.isEmpty
            .flatMap { if (it) Single.just(it) else Single.error(EmailExistsEx()) }
            .flatMapCompletable {
                Completable.mergeArray(
                    userRepo.insert(user),
                    nav.toLogin()
                )
            }
        }

}