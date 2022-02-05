package com.matthew.statefulbread.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.matthew.statefulbread.App
import com.matthew.statefulbread.TAG
import com.matthew.statefulbread.databinding.ActivitySplashBinding
import com.matthew.statefulbread.service.IPrefs

class Splash : AppCompatActivity() {

    private val prefs: IPrefs by lazy { App.castToApp(this).prefs }
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        Log.d(TAG, "onCreate")
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        if (prefs.getString("email") != "") navToHome() else navToLogin()
    }

    private fun navToHome() = startActivity(Intent(this, Home::class.java))

    private fun navToLogin() = startActivity(Intent(this, Login::class.java))

}