package com.matthew.statefulbread.view.main

import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.repo.ITheme
import com.matthew.statefulbread.repo.INav
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test

class MainVMTest: BaseTest() {

    @MockK lateinit var theme: ITheme
    @MockK lateinit var nav: INav

    private lateinit var mainVM: MainVM

    @Before override fun setUp() {
        super.setUp()
        mainVM = MainVM(theme, nav)
    }

    @Test fun on_frag_selected_should_correctly_match_titles_to_frags() {
        mainVM.onFragSelected("Home").test().dispose()
            .run { verify(exactly = 1) { nav.toHome() } }

        mainVM.onFragSelected("Search").test().dispose()
            .run { verify(exactly = 1) { nav.toSearch() } }

        mainVM.onFragSelected("Favorites").test().dispose()
            .run { verify(exactly = 1) { nav.toFavorites() } }

        mainVM.onFragSelected("Settings").test().dispose()
            .run { verify(exactly = 1) { nav.toSettings() } }
    }

    @Test fun on_frag_selected_should_complete_if_no_match() {
        mainVM.onFragSelected("").test().dispose()
            .run { verify(exactly = 0) { nav.toHome() } }
            .run { verify(exactly = 0) { nav.toSearch() } }
            .run { verify(exactly = 0) { nav.toFavorites() } }
            .run { verify(exactly = 0) { nav.toSettings() } }
    }

    @Test fun on_title_change_should_call_nav_get_current_title() {
        every { nav.getCurrentTitle() } returns Observable.just("ASDF")

        mainVM.onTitleChange().test().assertValue("ASDF").dispose().run {
            verify(exactly = 1) { nav.getCurrentTitle() }
        }
    }

    @Test fun on_toggle_daynight_mode_call_theme_toggle_daynight_mode() {
        every { theme.toggleDarkMode() } returns Completable.complete()

        mainVM.toggleDayNightMode().test().dispose().run {
            verify(exactly = 1) { theme.toggleDarkMode() }
        }
    }

}
