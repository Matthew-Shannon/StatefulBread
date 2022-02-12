package com.matthew.statefulbread.view.auth

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.jakewharton.rxbinding4.view.longClicks
import com.matthew.statefulbread.core.view.BaseActivity
import com.matthew.statefulbread.databinding.SplashBinding
import com.matthew.statefulbread.repo.*
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@AndroidEntryPoint
class Splash : BaseActivity<SplashBinding>(SplashBinding::inflate) {

    @Inject lateinit var splashVM: SplashVM

    override fun onCreate(bundle: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(bundle)
    }

    public override fun onResume() {
        super.onResume()

        //splashVM.clearStorage().subscribe().addTo(disposable)

        splashVM
            .initDayNightMode()
            .subscribe().addTo(disposable)

        binding.root.longClicks()
            .flatMapCompletable { splashVM.toggleDayNightMode() }
            .subscribe().addTo(disposable)

        splashVM.checkAuthorization()
            .subscribe().addTo(disposable)
    }

}

class SplashVM @Inject constructor(private val prefs: IPrefs, private val storage: IStorage, private val theme: ITheme, @SplashNav private val nav: INav) {

    fun navToMain(): Completable = Completable.fromAction(nav::toMain)

    fun navToLogin(): Completable = Completable.fromAction(nav::toLogin)

    fun checkAuthorization(): Completable = prefs
        .getAuthStatus()
        .flatMapCompletable { if (it) navToMain() else navToLogin() }

    fun clearStorage(): Completable = storage.clear().andThen(prefs.clear())

    fun initDayNightMode(): Completable = theme.initDayNightMode()

    fun toggleDayNightMode(): Completable = theme.toggleDarkMode()

}