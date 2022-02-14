package com.matthew.statefulbread.view.auth

import android.annotation.SuppressLint
import com.jakewharton.rxbinding4.view.longClicks
import com.matthew.statefulbread.core.view.BaseActivity
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.SplashNav
import com.matthew.statefulbread.databinding.SplashBinding
import com.matthew.statefulbread.service.IPrefs
import com.matthew.statefulbread.service.IStorage
import com.matthew.statefulbread.service.ITheme
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<SplashBinding>(SplashBinding::inflate) {

    @Inject lateinit var splashVM: SplashVM

    public override fun onResume() {
        super.onResume()
        disposable.add(splashVM.initDayNightMode()
            .subscribe())

        disposable.add(binding.root.longClicks()
            .flatMapCompletable { splashVM.toggleDayNightMode() }
            .subscribe())

        disposable.add(Completable.complete()
            //.andThen(vm.clearStorage())
            .andThen(splashVM.checkAuthorization())
            .subscribe())
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