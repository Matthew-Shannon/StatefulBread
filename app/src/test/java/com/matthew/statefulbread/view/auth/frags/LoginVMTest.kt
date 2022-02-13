package com.matthew.statefulbread.view.auth.frags

import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.core.view.INav
import com.matthew.statefulbread.repo.IPrefs
import com.matthew.statefulbread.repo.IStorage
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

    @Test fun on_nav_to_register() {
        loginVM.navToRegister().test().dispose()
            .run { verify(exactly = 1) { nav.toRegister() } }
    }

    @Test fun on_validate() {
        val user: User = mockk()

        every { user.email } returns ""
        loginVM.onValidate(user).test()
            .assertError(EmailBlankEx::class.java).dispose()

        every { user.email } returns "b"
        loginVM.onValidate(user).test()
            .assertError(EmailInvalidEx::class.java).dispose()

        every { user.email } returns "b@"
        every { user.password } returns ""
        loginVM.onValidate(user).test()
            .assertError(PasswordBlankEx::class.java).dispose()

        every { user.password } returns "c"
        loginVM.onValidate(user).test()
            .assertValue(user).dispose()
    }

    @Test fun on_submit() {
        var user: User = mockk()
        val userRepo: UserDao = mockk<UserDao>()
            .also { every { storage.userRepo() } returns Single.just(it) }

        loginVM.onSubmit(user).test()
            .assertError(Exception::class.java).dispose()
            .run { verify(exactly = 0) { nav.toMain() } }

        every { userRepo.findByCredentials(any(), any()) } returns Maybe.error(Exception())
        loginVM.onSubmit(user).test()
            .assertError(Exception::class.java).dispose()
            .run { verify(exactly = 0) { nav.toMain() } }

        user = User.empty()
        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(user)
        loginVM.onSubmit(user).test()
            .assertError(IncorrectCredentialsEx::class.java).dispose()
            .run { verify(exactly = 0) { nav.toMain() } }

        user = User(1, 2, "a", "b", "c", "d")
        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(user)
        every { prefs.setOwnerEmail(any()) } returns Completable.complete()
        every { prefs.setAuthStatus(any()) } returns Completable.complete()
        every { nav.toMain() } returns Completable.complete()

        loginVM.onSubmit(user).test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { nav.toMain() } }
    }

    @Test fun on_attempt() {
        val userRepo: UserDao = mockk<UserDao>()
            .also { every { storage.userRepo() } returns Single.just(it) }

        var data = mapOf<String,String>()
        loginVM.onAttempt(data).test()
            .assertError(EmailBlankEx::class.java).dispose()
            .run { verify(exactly = 0) { nav.toMain() } }

        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(User.empty())
        data = mapOf("email" to "b@", "password" to "d")
        loginVM.onAttempt(data).test()
            .assertError(IncorrectCredentialsEx::class.java).dispose()
            .run { verify(exactly = 0) { nav.toMain() } }

        every { prefs.setOwnerEmail(any()) } returns Completable.complete()
        every { prefs.setAuthStatus(any()) } returns Completable.complete()
        every { nav.toMain() } returns Completable.complete()
        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(User(1, 2, "a", "b", "c", "d"))
        data = mapOf("id" to "1", "age" to "2", "name" to "a", "email" to "b@", "zipCode" to "c", "password" to "d")
        loginVM.onAttempt(data).test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { nav.toMain() } }
    }

}
