package com.matthew.statefulbread.view.main

import android.view.MenuItem
import com.jakewharton.rxbinding4.material.itemSelections
import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.view.BaseActivity
import com.matthew.statefulbread.databinding.MainBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class Main : BaseActivity<MainBinding>(MainBinding::inflate) {

    override fun onResume() {
        super.onResume()

        binding.bottomNavigation.selectedItemId = R.id.menu_settings
        binding.bottomNavigation.itemSelections()
            .map(MenuItem::getItemId)
            .subscribe(::onNavTo)
            .addTo(disposable)
    }

    private fun onNavTo(id: Int) {
        when(id) {
            R.id.menu_home -> navService.toHome(R.id.container, binding.topAppBar::setTitle)
            R.id.menu_search -> navService.toSearch(R.id.container, binding.topAppBar::setTitle)
            R.id.menu_favorites -> navService.toFavorites(R.id.container, binding.topAppBar::setTitle)
            R.id.menu_settings -> navService.toSettings(R.id.container, binding.topAppBar::setTitle)
        }
    }

}