package com.matthew.statefulbread.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.matthew.statefulbread.view.Home
import com.matthew.statefulbread.view.Settings
import com.matthew.statefulbread.view.auth.Login
import com.matthew.statefulbread.view.auth.Logout
import com.matthew.statefulbread.view.auth.Register
import com.matthew.statefulbread.view.auth.Splash

interface INav {
    fun toSplash(context: Context)
    fun toRegister(context: Context)
    fun toLogin(context: Context)
    fun toLogout(context: Context)
    fun toHome(context: Context)
    fun toSettings(context: Context)
    fun goBack(context: Context)
}

class Nav : INav {

    override fun goBack(context: Context) { (context as? Activity)?.onBackPressed() }
    override fun toSplash(context: Context) = navTo(context, Splash::class.java)
    override fun toRegister(context: Context) = navTo(context, Register::class.java)
    override fun toLogin(context: Context) = navToAndClear(context, Login::class.java)
    override fun toLogout(context: Context) = navTo(context, Logout::class.java)
    override fun toHome(context: Context) = navToAndClear(context, Home::class.java)
    override fun toSettings(context: Context) = navTo(context, Settings::class.java)
    private fun navTo(context: Context, clazz: Class<*>) = context.startActivity(Intent(context, clazz))
    private fun navToAndClear(context: Context, clazz: Class<*>) = context.startActivity(Intent(context, clazz).addFlags(FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_NEW_TASK))

    companion object {
        fun def(): INav = Nav()
    }

}