package com.matthew.statefulbread.view.main.frags

import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.core.view.INav
import com.matthew.statefulbread.repo.IPrefs
import com.matthew.statefulbread.repo.IStorage
import com.matthew.statefulbread.repo.ITheme
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

    @MockK lateinit var storage: IStorage
    @MockK lateinit var prefs: IPrefs
    @MockK lateinit var theme: ITheme
    @MockK lateinit var nav: INav

    private lateinit var settingsVM: SettingsVM

    @Before override fun setUp() {
        super.setUp()
        settingsVM = SettingsVM(prefs, storage, theme, nav)
    }

    @Test fun on_logout_should_fail_if_storage_fail() {
        every { storage.clear() } returns Completable.error(Exception())
        settingsVM.onLogout().test()
            .assertError(Exception::class.java).dispose()
            .run { verify(exactly = 1) { storage.clear() } }
            .run { verify(exactly = 0) { prefs.clear() } }
            .run { verify(exactly = 0) { nav.toSplash() } }

        every { storage.clear() } returns Completable.complete()
        every { prefs.clear() } returns Completable.error(Exception())
        settingsVM.onLogout().test()
            .assertError(Exception::class.java).dispose()
            .run { verify(exactly = 2) { storage.clear() } }
            .run { verify(exactly = 1) { prefs.clear() } }
            .run { verify(exactly = 0) { nav.toSplash() } }

        every { prefs.clear() } returns Completable.complete()
        every { nav.toSplash() } returns Completable.complete()
        settingsVM.onLogout().test().dispose()
            .run { verify(exactly = 3) { storage.clear() } }
            .run { verify(exactly = 2) { prefs.clear() } }
            .run { verify(exactly = 1) { nav.toSplash() } }
    }

    @Test fun on_get_user() {
        every { prefs.getOwnerEmail() } returns Single.error(Exception())
        every { storage.userRepo() } returns Single.error(Exception())
        settingsVM.getUser().test()
            .assertError(Exception::class.java).dispose()
            .run { verify(exactly = 1) { prefs.getOwnerEmail() } }
            .run { verify(exactly = 0) { storage.userRepo() } }

        every { prefs.getOwnerEmail() } returns Single.just("asdf")
        settingsVM.getUser().test()
            .assertError(Exception::class.java).dispose()
            .run { verify(exactly = 2) { prefs.getOwnerEmail() } }
            .run { verify(exactly = 1) { storage.userRepo() } }

        val userDao: UserDao = mockk()
        every { storage.userRepo() } returns Single.just(userDao)

        every { userDao.findByEmail(any()) } returns Maybe.error(Exception())
        settingsVM.getUser().test()
            .assertError(Exception::class.java).dispose()
            .run { verify(exactly = 3) { prefs.getOwnerEmail() } }
            .run { verify(exactly = 2) { storage.userRepo() } }
            .run { verify(exactly = 1) { userDao.findByEmail(any()) } }

        every { userDao.findByEmail(any()) } returns Maybe.just(mockk<User>())
        settingsVM.getUser().test().dispose()
            .run { verify(exactly = 4) { prefs.getOwnerEmail() } }
            .run { verify(exactly = 3) { storage.userRepo() } }
            .run { verify(exactly = 2) { userDao.findByEmail(any()) } }

        settingsVM.getUser().test().values().size > 0
    }

    @Test fun on_toggle_day_night_mode() {
        every { theme.toggleDayNightMode() } returns Completable.complete()

        settingsVM.toggleDayNightMode().test().dispose()
            .run { verify(exactly = 1) { theme.toggleDayNightMode() } }
    }

    @Test fun on_get_day_night_mode() {
        every { prefs.getDayNightMode() } returns Single.just(true)

        settingsVM.getDayNightMode().test().dispose()
            .run { verify(exactly = 1) { prefs.getDayNightMode() } }
    }

}
