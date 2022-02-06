package com.matthew.statefulbread.view

import android.os.Bundle
import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.HomeBinding

class Home : BaseActivity<HomeBinding>(HomeBinding::inflate) {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        supportActionBar?.title = "Home"
    }

    override fun onStart() {
        super.onStart()
        setupBottomNav()
    }

    private fun setupBottomNav()  {
        binding.bottomNavigation.selectedItemId = R.id.menu_home

        val badge = binding.bottomNavigation.getOrCreateBadge(R.id.favorites)
        badge.isVisible = true
        badge.number = 99

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_home -> {
                    true
                }
                R.id.search -> {
                    true
                }
                R.id.favorites -> {
                    val badgeDrawable = binding.bottomNavigation.getBadge(R.id.favorites)
                    badgeDrawable?.isVisible = false
                    badgeDrawable?.clearNumber()
                    true
                }
                R.id.settings -> {
                    true
                }
                else -> false
            }
        }

        //binding.settingsButton.setOnClickListener { nav.toSettings(this) }
    }

}