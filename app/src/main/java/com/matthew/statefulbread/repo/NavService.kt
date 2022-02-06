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
import java.util.function.Consumer

interface INavService {
    fun toSplash()
    fun toRegister(id: Int)
    fun toLogin(id: Int)
    fun toMain()
    fun toHome(id: Int, onTitle: Consumer<String>)
    fun toSearch(id: Int, onTitle: Consumer<String>)
    fun toFavorites(id: Int, onTitle: Consumer<String>)
    fun toSettings(id: Int, onTitle: Consumer<String>)
}

class NavService(val activity: Activity) : INavService {

    override fun toSplash() = launchActivity(Splash::class.java)
    override fun toRegister(id: Int) = launchFragment(Register(), id)
    override fun toLogin(id: Int) = launchFragment(Login(), id)

    override fun toMain() = launchActivity(Main::class.java)
    override fun toHome(id: Int, onTitle: Consumer<String>) = launchFragment(Home(), id, onTitle)
    override fun toSearch(id: Int, onTitle: Consumer<String>) =  launchFragment(Search(), id, onTitle)
    override fun toFavorites(id: Int, onTitle: Consumer<String>) = launchFragment(Favorites(), id, onTitle)
    override fun toSettings(id: Int, onTitle: Consumer<String>) = launchFragment(Settings(), id, onTitle)


    private fun launchActivity(clazz: Class<*>) {
        activity.startActivity(Intent(activity, clazz))
        activity.finishAffinity()
    }

    private fun launchFragment(fragment: Fragment, id: Int, onTitle: Consumer<String> = Consumer<String> { }) {
        onTitle.accept(fragment.javaClass.simpleName)
        (activity as AppCompatActivity)
            .supportFragmentManager
            .beginTransaction()
            .replace(id, fragment)
            .commit()
    }

}