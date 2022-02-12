package com.matthew.statefulbread.repo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.matthew.statefulbread.view.auth.Splash
import com.matthew.statefulbread.view.auth.frags.Login
import com.matthew.statefulbread.view.auth.frags.Register
import com.matthew.statefulbread.view.main.Main
import com.matthew.statefulbread.view.main.frags.Favorites
import com.matthew.statefulbread.view.main.frags.Home
import com.matthew.statefulbread.view.main.frags.Search
import com.matthew.statefulbread.view.main.frags.Settings
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.BINARY

@Qualifier @Retention(BINARY) annotation class SplashNav
@Qualifier @Retention(BINARY) annotation class MainNav

interface INav {
    fun toSplash(): Completable
    fun toRegister(): Completable
    fun toLogin(): Completable
    fun toMain(): Completable
    fun toHome()
    fun toSearch()
    fun toFavorites()
    fun toSettings()
    fun getCurrentTitle(): Observable<String>
}

class Nav(val activity: Activity, val id: Int) : INav {
    private val currentTitle: PublishSubject<String> = PublishSubject.create()

    override fun getCurrentTitle(): Observable<String> = currentTitle

    override fun toSplash(): Completable = Completable.fromAction { launchActivity(Splash::class.java) }
    override fun toRegister(): Completable = Completable.fromAction { launchFragment(Register::class.java) }
    override fun toLogin(): Completable = Completable.fromAction { launchFragment(Login::class.java) }

    override fun toMain(): Completable = Completable.fromAction { launchActivity(Main::class.java) }
    override fun toHome() = launchFragment(Home::class.java)
    override fun toSearch() =  launchFragment(Search::class.java)
    override fun toFavorites() = launchFragment(Favorites::class.java)
    override fun toSettings() = launchFragment(Settings::class.java)

    private fun launchActivity(clazz: Class<out Activity>) {
        activity.startActivity(Intent(activity, clazz))
        activity.finishAffinity()
    }

    private fun launchFragment(clazz: Class<out Fragment>) {
        currentTitle.onNext(clazz.name.split(".").last())
        (activity as AppCompatActivity)
            .supportFragmentManager
            .beginTransaction()
            .replace(id, clazz, null)
            .commit()
    }

}

