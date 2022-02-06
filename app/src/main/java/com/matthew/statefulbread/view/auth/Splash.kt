package com.matthew.statefulbread.view.auth

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.view.BaseActivity
import com.matthew.statefulbread.databinding.SplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Splash : BaseActivity<SplashBinding>(SplashBinding::inflate) {

    override fun onCreate(bundle: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(bundle)
    }

    override fun onResume() {
        super.onResume()
        if (prefsService.getString("password") != "") navService.toMain()
        else navService.toLogin(R.id.splash_container)
    }

}