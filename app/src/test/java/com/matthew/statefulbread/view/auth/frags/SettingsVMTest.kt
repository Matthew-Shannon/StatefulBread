package com.matthew.statefulbread.view.auth.frags

import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.repo.*
import com.matthew.statefulbread.view.main.frags.SettingsVM
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
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

    @Test fun on_logout_should_clear_prefs_and_nav_to_splash() {
        every { prefs.clear() } returns Completable.complete()
        every { storage.clear() } returns Completable.complete()

        settingsVM.onLogout().test().run {
            verify(exactly = 1) { prefs.clear() }
            verify(exactly = 1) { storage.clear() }
            verify(exactly = 1) { nav.toSplash() }
        }
    }

//    @Test fun on_load_should_fetch_user_data() {
//        //every { storage.getUser() } returns Observable.just(listOf("Hello", "World"))
//
//        settingsVM.getUser().test().assertValue(listOf("Hello", "World"))
//    }

}
