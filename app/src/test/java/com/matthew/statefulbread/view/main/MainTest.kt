package com.matthew.statefulbread.view.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.core.MockApp
import com.matthew.statefulbread.core.RoboActivityRule
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.ITheme
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

class MainVMTest: BaseTest() {

    @MockK lateinit var theme: ITheme
    @MockK lateinit var nav: INav

    private lateinit var mainVM: MainVM

    @Before override fun setUp() {
        super.setUp()
        mainVM = MainVM(theme, nav)
    }

    @Test fun on_frag_selected_should_correctly_match_titles_to_frags() {
        every { nav.toHome() } returns Completable.complete()
        every { nav.toSearch() } returns Completable.complete()
        every { nav.toCategories() } returns Completable.complete()
        every { nav.toFavorites() } returns Completable.complete()
        every { nav.toSettings() } returns Completable.complete()

        mainVM.onFragSelected("Home").test().dispose()
            .run { verify(exactly = 1) { nav.toHome() } }

        mainVM.onFragSelected("Search").test().dispose()
            .run { verify(exactly = 1) { nav.toSearch() } }

        mainVM.onFragSelected("Categories").test().dispose()
            .run { verify(exactly = 1) { nav.toCategories() } }

        mainVM.onFragSelected("Favorites").test().dispose()
            .run { verify(exactly = 1) { nav.toFavorites() } }

        mainVM.onFragSelected("Settings").test().dispose()
            .run { verify(exactly = 1) { nav.toSettings() } }
    }

    @Test fun on_frag_selected_should_complete_if_no_match() {
        mainVM.onFragSelected("").test().dispose()
            .run { verify(exactly = 0) { nav.toHome() } }
            .run { verify(exactly = 0) { nav.toSearch() } }
            .run { verify(exactly = 0) { nav.toCategories() } }
            .run { verify(exactly = 0) { nav.toFavorites() } }
            .run { verify(exactly = 0) { nav.toSettings() } }
    }

    @Test fun on_title_change_should_call_nav_get_current_title() {
        every { nav.getCurrentTitle() } returns Observable.just("ASDF")

        mainVM.onTitleChange().test().assertValue("ASDF").dispose()
            .run { verify(exactly = 1) { nav.getCurrentTitle() } }
    }

    @Test fun on_toggle_daynight_mode_call_theme_toggle_daynight_mode() {
        every { theme.toggleDayNightMode() } returns Completable.complete()

        mainVM.toggleDayNightMode().test().dispose()
            .run { verify(exactly = 1) { theme.toggleDayNightMode() } }
    }

}

@RunWith(AndroidJUnit4::class)
@Config(application = MockApp::class)
class MainActivityTest: BaseTest() {

    @Rule @JvmField var robo = RoboActivityRule(MainActivity::class.java)

    @MockK lateinit var theme: ITheme
    @MockK lateinit var nav: INav

    @Before override fun setUp() {
        super.setUp()
        robo.controller.get().mainVM = MainVM(theme, nav)
        every { nav.toHome() } returns Completable.complete()
        every { nav.getCurrentTitle() } returns Observable.just("")
    }

    @Test
    fun on_root_long_click() {
        every { theme.toggleDayNightMode() } returns Completable.complete()

        robo.controller.setup().get().binding.root.performLongClick()
        verify(exactly = 1) { theme.toggleDayNightMode() }
    }

    @Test
    fun on_title_changed() {
        every { nav.toHome() } returns Completable.complete()
        every { nav.toSearch() } returns Completable.complete()
        every { nav.toCategories() } returns Completable.complete()
        every { nav.toFavorites() } returns Completable.complete()
        every { nav.toSettings() } returns Completable.complete()

        robo.controller.setup()
        robo.controller.get().binding.bottomNavigation.selectedItemId = R.id.menu_home
        verify(exactly = 2) { nav.toHome() }

        robo.controller.get().binding.bottomNavigation.selectedItemId = R.id.menu_search
        verify(exactly = 1) { nav.toSearch() }

        robo.controller.get().binding.bottomNavigation.selectedItemId = R.id.menu_categories
        verify(exactly = 1) { nav.toCategories() }

        robo.controller.get().binding.bottomNavigation.selectedItemId = R.id.menu_favorites
        verify(exactly = 1) { nav.toFavorites() }

        robo.controller.get().binding.bottomNavigation.selectedItemId = R.id.menu_settings
        verify(exactly = 1) { nav.toSettings() }
    }

}
