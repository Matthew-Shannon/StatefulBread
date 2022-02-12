package com.matthew.statefulbread.view.auth

import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.repo.ITheme
import com.matthew.statefulbread.repo.INav
import com.matthew.statefulbread.repo.IPrefs
import com.matthew.statefulbread.repo.IStorage
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
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

        splashVM.checkAuthorization().test().run {
            verify(exactly = 1) { nav.toMain() }
            verify(exactly = 0) { nav.toLogin() }
        }
    }

    @Test fun on_startup_when_no_saved_password_go_to_login() {
        every { prefs.getAuthStatus() } returns Single.just(false)

        splashVM.checkAuthorization().test().run {
            verify(exactly = 0) { nav.toMain() }
            verify(exactly = 1) { nav.toLogin() }
        }
    }

}
