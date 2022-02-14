package com.matthew.statefulbread.view.auth.frags

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.core.MockApp
import com.matthew.statefulbread.service.INav
import com.matthew.statefulbread.service.IPrefs
import com.matthew.statefulbread.service.IStorage
import com.matthew.statefulbread.service.model.User
import com.matthew.statefulbread.service.model.UserDao
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

class LoginVMTest: BaseTest() {

    private lateinit var loginVM: LoginVM
    @MockK lateinit var storage: IStorage
    @MockK lateinit var prefs: IPrefs
    @MockK lateinit var nav: INav

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
        val userRepo: UserDao = mockk<UserDao>().also { every { storage.userRepo() } returns Single.just(it) }

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
        val userRepo: UserDao = mockk<UserDao>().also { every { storage.userRepo() } returns Single.just(it) }

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
        val user = User(1, 2, "a", "b@", "c", "d")
        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(user)
        data = User.explode(user)
        loginVM.onAttempt(data).test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { nav.toMain() } }
    }

}


@RunWith(AndroidJUnit4::class)
@Config(application = MockApp::class)
class LoginFragmentTest: BaseTest() {

    @MockK lateinit var storage: IStorage
    @MockK lateinit var prefs: IPrefs
    @MockK lateinit var nav: INav

    lateinit var scenario: FragmentScenario<LoginFragment>

    @Before override fun setUp() {
        super.setUp()
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_StatefulBread) {
            LoginFragment().apply { loginVM = LoginVM(prefs, storage, nav) }
        }
    }

    @After override fun tearDown() {
        super.tearDown()
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test fun on_error() {
        scenario.onFragment {

            it.onError(Exception())
            Assert.assertEquals(it.binding.emailEditText.error, null)
            Assert.assertEquals(it.binding.passwordEditText.error, null)

            it.onError(EmailBlankEx())
            Assert.assertEquals(it.binding.emailEditText.error, EmailBlankEx().message)

            it.onError(EmailInvalidEx())
            Assert.assertEquals(it.binding.emailEditText.error, EmailInvalidEx().message)

            it.onError(EmailExistsEx())
            Assert.assertEquals(it.binding.emailEditText.error, EmailExistsEx().message)

            it.onError(PasswordBlankEx())
            Assert.assertEquals(it.binding.passwordEditText.error, PasswordBlankEx().message)

            it.onError(IncorrectCredentialsEx())
            Assert.assertEquals(it.binding.passwordEditText.error, IncorrectCredentialsEx().message)


        }
    }

    @Test fun on_nav_to_register() {
        every { nav.toRegister() } returns Completable.complete()

        scenario.onFragment {
            it.binding.registerButton.performClick()
            verify(exactly = 1) { nav.toRegister() }

        }
    }

    @Test fun on_data() {
        scenario.onFragment {

            it.binding.emailEditText.setText("HELLO")
            it.binding.passwordEditText.setText("WORLD")
            it.getData().also { data ->
                Assert.assertEquals(data["email"], "HELLO")
                Assert.assertEquals(data["password"], "WORLD")
            }

            it.binding.emailEditText.setText("")
            it.binding.passwordEditText.setText("")
            it.getData().also { data ->
                Assert.assertEquals(data["email"], "")
                Assert.assertEquals(data["password"], "")
            }
        }
    }

    @Test fun on_submit() {
        every { prefs.setOwnerEmail(any()) } returns Completable.complete()
        every { prefs.setAuthStatus(any()) } returns Completable.complete()
        every { nav.toMain() } returns Completable.complete()

        val userRepo: UserDao = mockk<UserDao>().also { every { storage.userRepo() } returns Single.just(it) }
        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(User(1, 2, "a", "b@", "c", "d"))

        scenario.onFragment {
            it.binding.emailEditText.setText("b@")
            it.binding.passwordEditText.setText("a")

            it.onSubmit(it.getData())
            verify(exactly = 1) { nav.toMain() }

            it.binding.loginButton.performClick()
            verify(exactly = 2) { nav.toMain() }
        }
    }

}