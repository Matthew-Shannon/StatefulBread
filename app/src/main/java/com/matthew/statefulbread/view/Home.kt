package com.matthew.statefulbread.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.matthew.statefulbread.App
import com.matthew.statefulbread.TAG
import com.matthew.statefulbread.databinding.ActivityHomeBinding
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.IPrefs

class Home : AppCompatActivity() {

    private val prefs: IPrefs by lazy { App.castToApp(this).prefs }
    private val nav: INav by lazy { App.castToApp(this).nav }
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        Log.d(TAG, "onCreate")
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Home"
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        binding.settingsButton.setOnClickListener { nav.toSettings(this) }
    }

}