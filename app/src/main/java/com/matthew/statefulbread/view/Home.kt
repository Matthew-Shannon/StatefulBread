package com.matthew.statefulbread.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.matthew.statefulbread.App
import com.matthew.statefulbread.R
import com.matthew.statefulbread.TAG
import com.matthew.statefulbread.databinding.ActivityHomeBinding
import com.matthew.statefulbread.databinding.ActivityLoginBinding
import com.matthew.statefulbread.service.IPrefs

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val prefs: IPrefs by lazy { App.castToApp(this).prefs }

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
        binding.settingsButton.setOnClickListener { navToSettings() }
    }

    private fun navToSettings() = startActivity(Intent(this, Settings::class.java))

}