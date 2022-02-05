package com.matthew.statefulbread.view.auth

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.matthew.statefulbread.App
import com.matthew.statefulbread.TAG
import com.matthew.statefulbread.databinding.ActivityLogoutBinding
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.IPrefs

class Logout : AppCompatActivity() {

    private val prefs: IPrefs by lazy { App.castToApp(this).prefs }
    private val nav: INav by lazy { App.castToApp(this).nav }
    private lateinit var binding: ActivityLogoutBinding

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        Log.d(TAG, "onCreate")
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
        prefs.clear()
        nav.toSplash(this)
    }

}