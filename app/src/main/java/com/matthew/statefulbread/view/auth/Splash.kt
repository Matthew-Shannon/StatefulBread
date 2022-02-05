package com.matthew.statefulbread.view.auth

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.SplashBinding

class Splash : BaseActivity<SplashBinding>(SplashBinding::inflate) {

    override fun onCreate(bundle: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(bundle)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
//        if (prefs.getString("password") != "") nav.toHome(this) else nav.toLogin(this)
//        finish()
    }

}