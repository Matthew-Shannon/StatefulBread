package com.matthew.statefulbread.view.auth

import android.os.Bundle
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.ActivitySplashBinding

class Splash : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        if (prefs.getString("password") != "") nav.toHome(this) else nav.toLogin(this)
        finish()
    }

}