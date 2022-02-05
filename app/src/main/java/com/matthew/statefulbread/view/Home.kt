package com.matthew.statefulbread.view

import android.os.Bundle
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.HomeBinding

class Home : BaseActivity<HomeBinding>(HomeBinding::inflate) {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        supportActionBar?.title = "Home"
    }

    override fun onStart() {
        super.onStart()
        //binding.settingsButton.setOnClickListener { nav.toSettings(this) }
    }

}