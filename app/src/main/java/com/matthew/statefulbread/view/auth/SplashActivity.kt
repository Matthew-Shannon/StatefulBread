package com.matthew.statefulbread.view.auth

import com.jakewharton.rxbinding4.view.longClicks
import com.matthew.statefulbread.core.view.BaseActivity
import com.matthew.statefulbread.core.view.INav
import com.matthew.statefulbread.core.view.SplashNav
import com.matthew.statefulbread.databinding.SplashBinding
import com.matthew.statefulbread.repo.IPrefs
import com.matthew.statefulbread.repo.IStorage
import com.matthew.statefulbread.repo.ITheme
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity<SplashBinding>(SplashBinding::inflate) {

    @Inject lateinit var helper: SplashHelper

    public override fun onResume() {
        super.onResume()
        helper.onResume(disposable, binding)
    }

}

class SplashHelper @Inject constructor(private val vm: SplashVM) {

    fun onResume(disposable: CompositeDisposable, binding: SplashBinding) {
        vm.initDayNightMode()
            .subscribe().addTo(disposable)

        binding.root.longClicks()
            .flatMapCompletable { vm.toggleDayNightMode() }
            .subscribe().addTo(disposable)

        Completable.complete()
            //.andThen(splashVM.clearStorage())
            .andThen(vm.checkAuthorization())
            .subscribe().addTo(disposable)
    }

}

class SplashVM @Inject constructor(private val prefs: IPrefs, private val storage: IStorage, private val theme: ITheme, @SplashNav private val nav: INav) {

    fun checkAuthorization(): Completable = prefs.getAuthStatus()
        .flatMapCompletable {
            if (it) Completable.defer(nav::toMain)
            else Completable.defer(nav::toLogin)
//            Completable.defer(nav::toLogin)
        }

    fun clearStorage(): Completable = storage.clear().andThen(prefs.clear())

    fun initDayNightMode(): Completable = theme.initDayNightMode()

    fun toggleDayNightMode(): Completable = theme.toggleDayNightMode()

}