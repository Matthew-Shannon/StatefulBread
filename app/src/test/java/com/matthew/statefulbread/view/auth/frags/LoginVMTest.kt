package com.matthew.statefulbread.view.auth.frags

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

class LoginVMTest: BaseTest() {

    @MockK lateinit var prefs: IPrefs
    @MockK lateinit var storage: IStorage
    @MockK lateinit var nav: INav

    private lateinit var loginVM: LoginVM

    @Before override fun setUp() {
        super.setUp()
        loginVM = LoginVM(prefs, storage, nav)
    }

    @Test fun on_nav_to_register_called_should_call_nav_to_register() {
        loginVM.navToRegister().test().dispose().run {
            verify(exactly = 1) { nav.toRegister() }
        }
    }

    @Test fun on_submit_called_should_throw_exception_with_empty_user() {
        every { storage.userRepo() } returns Single.just(mockk<UserDao>().also {
            every { it.findByCredentials(any(), any()) } returns Maybe.error(Exception())
        })

        loginVM.onSubmit("asdf", "qwer").test().assertError(Exception::class.java).dispose().run {
            verify(exactly = 0) { nav.toMain() }
        }
    }

    @Test fun on_submit_called_should_succeed_with_non_empty_user() {
        val userRepo: UserDao = mockk<UserDao>().also {
            every { it.findByCredentials(any(), any()) } returns Maybe.just(mockk<User>().also {
                every { it.id } returns 1
            })
        }
        every { prefs.setOwnerID(any()) } returns Completable.complete()
        every { prefs.setAuthStatus(any()) } returns Completable.complete()
        every { storage.userRepo() } returns Single.just(userRepo)
        every { nav.toMain() } returns Completable.complete()

        loginVM.onSubmit("asdf", "qwer").test().assertNoErrors().dispose().run {
            verify(exactly = 1) { nav.toMain() }
        }

    }

}
