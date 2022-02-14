package com.matthew.statefulbread.view.auth

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.core.MockApp
import com.matthew.statefulbread.core.RoboActivityRule
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.IPrefs
import com.matthew.statefulbread.service.IStorage
import com.matthew.statefulbread.service.ITheme
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

class SplashActivityVMTest: BaseTest() {

    private lateinit var splashVM: SplashVM
    @MockK lateinit var storage: IStorage
    @MockK lateinit var prefs: IPrefs
    @MockK lateinit var theme: ITheme
    @MockK lateinit var nav: INav

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
        every { theme.toggleDayNightMode() } returns Completable.complete()

        splashVM.toggleDayNightMode().test().dispose()
            .run { verify(exactly = 1) { theme.toggleDayNightMode() } }
    }

}

@RunWith(AndroidJUnit4::class)
@Config(application = MockApp::class)
class SplashActivityTest: BaseTest() {

    @Rule @JvmField var robo = RoboActivityRule(SplashActivity::class.java)
    @MockK lateinit var storage: IStorage
    @MockK lateinit var prefs: IPrefs
    @MockK lateinit var theme: ITheme
    @MockK lateinit var nav: INav

    @Before override fun setUp() {
        super.setUp()
        every { nav.toMain() } returns Completable.complete()
        every { nav.toLogin() } returns Completable.complete()
        every { theme.initDayNightMode() } returns Completable.complete()
        robo.controller.get().splashVM = SplashVM(prefs, storage, theme, nav)
    }

    @Test fun when_auth_nav_to_Main() {
        every { theme.toggleDayNightMode() } returns Completable.complete()
        every { prefs.getAuthStatus() } returns Single.just(true)

        robo.controller.setup()
        verify(exactly = 1) { nav.toMain() }
    }

    @Test fun when_no_auth_nav_to_Register() {
        every { theme.toggleDayNightMode() } returns Completable.complete()
        every { prefs.getAuthStatus() } returns Single.just(false)

        robo.controller.setup()
        verify(exactly = 1) { nav.toLogin() }
    }

    @Test fun when_long_click_background_toggle_day_night_mode() {
        every { theme.toggleDayNightMode() } returns Completable.complete()
        every { prefs.getAuthStatus() } returns Single.just(true)

        robo.controller.setup().get().binding.root.performLongClick()
        verify(exactly = 1) { theme.toggleDayNightMode() }
    }

}