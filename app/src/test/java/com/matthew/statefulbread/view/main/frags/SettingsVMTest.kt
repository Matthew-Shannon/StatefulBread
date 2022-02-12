package com.matthew.statefulbread.view.main.frags

import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.repo.*
import com.matthew.statefulbread.repo.model.User
import com.matthew.statefulbread.repo.model.UserDao
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class SettingsVMTest: BaseTest() {

    @MockK lateinit var prefs: IPrefs
    @MockK lateinit var storage: IStorage
    @MockK lateinit var theme: ITheme
    @MockK lateinit var nav: INav

    private lateinit var settingsVM: SettingsVM

    @Before override fun setUp() {
        super.setUp()
        settingsVM = SettingsVM(prefs, storage, theme, nav)
    }

    @Test fun on_logout_should_fail_if_storage_fail() {
        every { storage.clear() } returns Completable.error(Exception())

        settingsVM.onLogout().test().assertError(Exception::class.java).dispose().run {
            verify(exactly = 1) { storage.clear() }
            verify(exactly = 0) { prefs.clear() }
            verify(exactly = 0) { nav.toSplash() }
        }
    }

    @Test fun on_logout_should_fail_if_prefs_fail() {
        every { storage.clear() } returns Completable.complete()
        every { prefs.clear() } returns Completable.error(Exception())

        settingsVM.onLogout().test().assertError(Exception::class.java).dispose().run {
            verify(exactly = 1) { storage.clear() }
            verify(exactly = 1) { prefs.clear() }
            verify(exactly = 0) { nav.toSplash() }
        }
    }

    @Test fun on_logout_should_clear_prefs_and_nav_to_splash() {
        every { prefs.clear() } returns Completable.complete()
        every { storage.clear() } returns Completable.complete()
        every { nav.toSplash() } returns Completable.complete()

        settingsVM.onLogout().test().dispose().run {
            verify(exactly = 1) { storage.clear() }
            verify(exactly = 1) { prefs.clear() }
            verify(exactly = 1) { nav.toSplash() }
        }
    }

    @Test fun on_get_user_should_fail_if_prefs_get_owner_id_fails() {
        every { prefs.getOwnerID() } returns Single.error(Exception())
        every { storage.userRepo() } returns Single.error(Exception())

        settingsVM.getUser().test().assertError(Exception::class.java).dispose().run {
            verify(exactly = 1) { prefs.getOwnerID() }
            verify(exactly = 0) { storage.userRepo() }
        }
    }

    @Test fun on_get_user_should_fail_if_user_repo_fails() {
        every { prefs.getOwnerID() } returns Single.just(1)
        every { storage.userRepo() } returns Single.error(Exception())

        settingsVM.getUser().test().assertError(Exception::class.java).dispose().run {
            verify(exactly = 1) { prefs.getOwnerID() }
            verify(exactly = 1) { storage.userRepo() }
        }
    }

    @Test fun on_get_user_should_fail_if_user_repo_find_by_id_fails() {
        val userDao: UserDao = mockk {
            every { findById(any()) } returns Maybe.error(Exception())
        }
        every { prefs.getOwnerID() } returns Single.just(1)
        every { storage.userRepo() } returns Single.just(userDao)

        settingsVM.getUser().test().assertError(Exception::class.java).dispose().run {
            verify(exactly = 1) { prefs.getOwnerID() }
            verify(exactly = 1) { storage.userRepo() }
            verify(exactly = 1) { userDao.findById(any()) }
        }
    }

    @Test fun on_get_user_should_succeed() {
        val user: User = mockk<User>()
        val userDao: UserDao = mockk {
            every { findById(any()) } returns Maybe.just(user)
        }
        every { prefs.getOwnerID() } returns Single.just(1)
        every { storage.userRepo() } returns Single.just(userDao)

        settingsVM.getUser().test().dispose().run {
            verify(exactly = 1) { prefs.getOwnerID() }
            verify(exactly = 1) { storage.userRepo() }
            verify(exactly = 1) { userDao.findById(any()) }
        }
    }



    @Test fun on_get_daynight_mode_call_theme_get_daynight_mode() {
        every { theme.getDarkMode() } returns Single.just(true)

        settingsVM.getDayNightMode().test().dispose().run {
            verify(exactly = 1) { theme.getDarkMode() }
        }
    }

    @Test fun on_toggle_daynight_mode_call_theme_toggle_daynight_mode() {
        every { theme.toggleDarkMode() } returns Completable.complete()

        settingsVM.toggleDayNightMode().test().dispose().run {
            verify(exactly = 1) { theme.toggleDarkMode() }
        }
    }

}
