package com.matthew.statefulbread.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.matthew.statefulbread.view.auth.SplashActivity
import com.matthew.statefulbread.view.auth.frags.LoginFragment
import com.matthew.statefulbread.view.auth.frags.RegisterFragment
import com.matthew.statefulbread.view.main.MainActivity
import com.matthew.statefulbread.view.main.frags.*
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
    fun toHome(): Completable
    fun toSearch(): Completable
    fun toCategories(): Completable
    fun toFavorites(): Completable
    fun toSettings(): Completable
    fun getCurrentTitle(): Observable<String>
}

class Nav(private val context: Context, private val fragmentManager: FragmentManager, private val id: Int) : INav {
    private val currentTitle: PublishSubject<String> = PublishSubject.create()

    override fun getCurrentTitle(): Observable<String> = currentTitle

    override fun toSplash(): Completable = launchActivity(SplashActivity::class.java)
    override fun toRegister(): Completable = launchFragment(RegisterFragment::class.java)
    override fun toLogin(): Completable = launchFragment(LoginFragment::class.java)

    override fun toMain(): Completable =  launchActivity(MainActivity::class.java)
    override fun toHome(): Completable = launchFragment(HomeFragment::class.java)
    override fun toSearch(): Completable =  launchFragment(SearchFragment::class.java)
    override fun toCategories(): Completable =  launchFragment(CategoriesFragment::class.java)
    override fun toFavorites(): Completable = launchFragment(FavoritesFragment::class.java)
    override fun toSettings(): Completable = launchFragment(SettingsFragment::class.java)

    private fun launchActivity(clazz: Class<out Activity>): Completable = Completable.fromAction {
        currentTitle.onNext(clazz.name.split(".").last().replace("Activity", ""))
        context.startActivity(Intent(context, clazz).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }

    private fun launchFragment(clazz: Class<out Fragment>): Completable = Completable.fromAction {
        currentTitle.onNext(clazz.name.split(".").last().replace("Fragment", ""))
        fragmentManager
            .beginTransaction()
            .setTransition(TRANSIT_FRAGMENT_FADE)
            .replace(id, clazz, null)
            .commit()
    }

}

