package com.matthew.statefulbread.view.main

import android.view.MenuItem
import com.jakewharton.rxbinding4.material.itemSelections
import com.jakewharton.rxbinding4.view.longClicks
import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.view.BaseActivity
import com.matthew.statefulbread.core.view.INav
import com.matthew.statefulbread.core.view.MainNav
import com.matthew.statefulbread.databinding.MainBinding
import com.matthew.statefulbread.repo.ITheme
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@AndroidEntryPoint
class Main : BaseActivity<MainBinding>(MainBinding::inflate) {

    @Inject lateinit var mainVM: MainVM

    override fun onResume() {
        super.onResume()

        mainVM.onTitleChange()
            .doOnNext(binding.topAppBar::setTitle)
            .subscribe().addTo(disposable)

        binding.root.longClicks()
            .flatMapCompletable { mainVM.toggleDayNightMode() }
            .subscribe().addTo(disposable)

        binding.bottomNavigation.selectedItemId = R.id.menu_settings

        binding.bottomNavigation.itemSelections()
            .map(MenuItem::getTitle)
            .flatMapCompletable(mainVM::onFragSelected)
            .subscribe().addTo(disposable)
    }

}

class MainVM @Inject constructor(private val theme: ITheme, @MainNav private val nav: INav) {

    fun onFragSelected(title: CharSequence): Completable = when(title) {
        "Home" -> nav.toHome()
        "Search" -> nav.toSearch()
        "Categories" -> nav.toCategories()
        "Favorites" -> nav.toFavorites()
        "Settings" -> nav.toSettings()
        else -> Completable.complete()
    }

    fun onTitleChange(): Observable<String> = nav.getCurrentTitle()

    fun toggleDayNightMode(): Completable = theme.toggleDarkMode()

}