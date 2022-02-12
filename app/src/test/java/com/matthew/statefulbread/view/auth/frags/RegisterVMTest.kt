package com.matthew.statefulbread.view.auth.frags

import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.repo.*
import com.matthew.statefulbread.repo.model.UserDao
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class RegisterVMTest: BaseTest() {

    @MockK lateinit var storage: IStorage
    @MockK lateinit var nav: INav

    private lateinit var registerVM: RegisterVM

    @Before override fun setUp() {
        super.setUp()
        registerVM = RegisterVM(storage, nav)
    }

    @Test fun on_nav_to_login_called_should_call_nav_to_login() {
        registerVM.navToLogin().test().dispose().run {
            verify(exactly = 1) { nav.toLogin() }
        }
    }

    @Test fun on_submit_called_should_throw_exception_with_empty_user() {
        every { storage.userRepo() } returns Single.just(mockk<UserDao>().also {
            every { it.insert(any()) } returns Completable.error(Exception())
        })

        registerVM.onSubmit(mockk()).test().assertError(Exception::class.java).dispose().run {
            verify(exactly = 0) { nav.toLogin() }
        }
    }

    @Test fun on_submit_called_should_complete_successfully() {
        every { nav.toLogin() } returns Completable.complete()
        every { storage.userRepo() } returns Single.just(mockk<UserDao>().also {
            every { it.insert(any()) } returns Completable.complete()
        })

        registerVM.onSubmit(mockk()).test().assertNoErrors().dispose().run {
            verify(exactly = 1) { nav.toLogin() }
        }
    }

}
