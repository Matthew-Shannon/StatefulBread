package com.matthew.statefulbread.view.auth

import android.os.Bundle
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.LogoutBinding

class Logout : BaseActivity<LogoutBinding>(LogoutBinding::inflate) {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        prefs.clear()
        nav.toSplash(this)
        finishAffinity()
    }

}