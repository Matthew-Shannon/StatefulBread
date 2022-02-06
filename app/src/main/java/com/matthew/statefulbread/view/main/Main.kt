package com.matthew.statefulbread.view.main

import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.BaseActivity
import com.matthew.statefulbread.databinding.MainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Main : BaseActivity<MainBinding>(MainBinding::inflate) {

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.setOnItemSelectedListener { onNavTo(it.itemId); true }
        binding.bottomNavigation.selectedItemId = R.id.menu_home
    }

    private fun onNavTo(id: Int) {
        when(id) {
            R.id.menu_home -> nav.toHome(R.id.container, binding.topAppBar::setTitle)
            R.id.search -> nav.toSearch(R.id.container, binding.topAppBar::setTitle)
            R.id.favorites -> nav.toFavorites(R.id.container, binding.topAppBar::setTitle)
            R.id.settings -> nav.toSettings(R.id.container, binding.topAppBar::setTitle)
        }
    }

}