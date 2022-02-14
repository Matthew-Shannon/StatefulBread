package com.matthew.statefulbread.view.main

import android.view.MenuItem
import com.jakewharton.rxbinding4.material.itemSelections
import com.jakewharton.rxbinding4.view.longClicks
import com.matthew.statefulbread.core.view.BaseActivity
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.MainNav
import com.matthew.statefulbread.databinding.MainBinding
import com.matthew.statefulbread.service.ITheme
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<MainBinding>(MainBinding::inflate) {

    @Inject lateinit var mainVM: MainVM

    override fun onResume() {
        super.onResume()

        disposable.add(binding.root.longClicks()
            .flatMapCompletable { mainVM.toggleDayNightMode() }
            .subscribe())

        disposable.add(mainVM.onTitleChange()
            .doOnNext(binding.topAppBar::setTitle)
            .subscribe())

        disposable.add(binding.bottomNavigation.itemSelections()
            .map(MenuItem::getTitle)
            .flatMapCompletable(mainVM::onFragSelected)
            .subscribe())
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

    fun toggleDayNightMode(): Completable = theme.toggleDayNightMode()

}