package com.matthew.statefulbread.view.auth

import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.core.view.INav
import com.matthew.statefulbread.repo.IPrefs
import com.matthew.statefulbread.repo.IStorage
import com.matthew.statefulbread.repo.ITheme
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class SplashVMTest: BaseTest() {

    @MockK lateinit var prefs: IPrefs
    @MockK lateinit var storage: IStorage
    @MockK lateinit var theme: ITheme
    @MockK lateinit var nav: INav

    private lateinit var splashVM: SplashVM

    @Before override fun setUp() {
        super.setUp()
        splashVM = SplashVM(prefs, storage, theme, nav)
    }

    @Test fun on_startup_when_saved_password_go_to_main() {
        every { prefs.getAuthStatus() } returns Single.just(true)

        splashVM.checkAuthorization().test().dispose()
            .run { verify(exactly = 1) { nav.toMain() } }
            .run { verify(exactly = 0) { nav.toLogin() } }
    }

    @Test fun on_startup_when_no_saved_password_go_to_login() {
        every { prefs.getAuthStatus() } returns Single.just(false)

        splashVM.checkAuthorization().test().dispose()
            .run { verify(exactly = 0) { nav.toMain() } }
            .run { verify(exactly = 1) { nav.toLogin() } }
    }

    @Test fun on_clear_storage_call_storage_clear_and_prefs_clear() {
        every { storage.clear() } returns Completable.complete()
        every { prefs.clear() } returns Completable.complete()

        splashVM.clearStorage().test().dispose()
            .run { verify(exactly = 1) { storage.clear() } }
            .run { verify(exactly = 1) { prefs.clear() } }
    }

    @Test fun on_init_daynight_mode_call_theme_init_daynight_mode() {
        every { theme.initDayNightMode() } returns Completable.complete()

        splashVM.initDayNightMode().test().dispose()
            .run { verify(exactly = 1) { theme.initDayNightMode() } }
    }

    @Test fun on_toggle_daynight_mode_call_theme_toggle_daynight_mode() {
        every { theme.toggleDarkMode() } returns Completable.complete()

        splashVM.toggleDayNightMode().test().dispose()
            .run { verify(exactly = 1) { theme.toggleDarkMode() } }
    }

}
