package com.matthew.statefulbread.view.auth.frags

import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.core.view.INav
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

class RegisterVMTest: BaseTest() {

    @MockK lateinit var storage: IStorage
    @MockK lateinit var nav: INav

    private lateinit var registerVM: RegisterVM

    @Before override fun setUp() {
        super.setUp()
        registerVM = RegisterVM(storage, nav)
    }

    @Test fun on_nav_to_login() {
        registerVM.navToLogin().test().dispose()
            .run { verify(exactly = 1) { nav.toLogin() } }
    }

    @Test fun on_validate() {
        val user: User = mockk()
        every { user.name } returns ""
        registerVM.onValidate(user).test()
            .assertError(NameBlankEx::class.java).dispose()

        every { user.name } returns "a"
        every { user.email } returns ""
        registerVM.onValidate(user).test()
            .assertError(EmailBlankEx::class.java).dispose()

        every { user.email } returns "b"
        registerVM.onValidate(user).test()
            .assertError(EmailInvalidEx::class.java).dispose()

        every { user.email } returns "b@"
        every { user.zipCode } returns ""
        registerVM.onValidate(user).test()
            .assertError(ZipCodeBlankEx::class.java).dispose()

        every { user.zipCode } returns "c"
        every { user.password } returns ""
        registerVM.onValidate(user).test()
            .assertError(PasswordBlankEx::class.java).dispose()

        every { user.password } returns "d"
        registerVM.onValidate(user).test()
            .assertValue(user).dispose()
    }

    @Test fun on_submit() {
        var user: User = mockk()
        val userRepo: UserDao = mockk<UserDao>()
            .also { every { storage.userRepo() } returns Single.just(it) }

        registerVM.onSubmit(user).test()
            .assertError(Exception::class.java).dispose()
            .run { verify(exactly = 0) { nav.toLogin() } }

        every { userRepo.findByCredentials(any(), any()) } returns Maybe.error(Exception())
        registerVM.onSubmit(user).test()
            .assertError(Exception::class.java).dispose()
            .run { verify(exactly = 0) { nav.toLogin() } }

        user = User(1, 2, "a", "b", "c", "d")
        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(user)
        registerVM.onSubmit(user).test()
            .assertError(EmailExistsEx::class.java).dispose()
            .run { verify(exactly = 0) { nav.toLogin() } }

        user = User.empty()
        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(user)
        every { userRepo.insert(any()) } returns Completable.complete()
        every { nav.toLogin() } returns Completable.complete()
        registerVM.onSubmit(user).test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { userRepo.insert(user) } }
            .run { verify(exactly = 1) { nav.toLogin() } }
    }

    @Test fun on_attempt() {
        val userRepo: UserDao = mockk<UserDao>()
            .also { every { storage.userRepo() } returns Single.just(it) }

        var data = mapOf<String,String>()
        registerVM.onAttempt(data).test()
            .assertError(NameBlankEx::class.java).dispose()
            .run { verify(exactly = 0) { nav.toLogin() } }

        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(User(1, 2, "a", "b", "c", "d"))
        data = mapOf("id" to "1", "age" to "2", "name" to "a", "email" to "b@", "zipCode" to "c", "password" to "d")
        registerVM.onAttempt(data).test()
            .assertError(EmailExistsEx::class.java).dispose()
            .run { verify(exactly = 0) { nav.toLogin() } }

        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(User.empty())
        every { userRepo.insert(any()) } returns Completable.complete()
        every { nav.toLogin() } returns Completable.complete()
        registerVM.onAttempt(data).test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { nav.toLogin() } }
    }

}
