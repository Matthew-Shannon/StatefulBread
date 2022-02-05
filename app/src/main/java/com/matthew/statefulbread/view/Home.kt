package com.matthew.statefulbread.view

import android.os.Bundle
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.ActivityHomeBinding

class Home : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Home"
    }

    override fun onStart() {
        super.onStart()
        binding.settingsButton.setOnClickListener { nav.toSettings(this) }
    }

}