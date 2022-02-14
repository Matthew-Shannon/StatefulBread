package com.matthew.statefulbread.view.auth.frags

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.matthew.statefulbread.R
import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.core.MockApp
import com.matthew.statefulbread.service.INav
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
        val userRepo: UserDao = mockk<UserDao>().also { every { storage.userRepo() } returns Single.just(it) }

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
        val userRepo: UserDao = mockk<UserDao>().also { every { storage.userRepo() } returns Single.just(it) }

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

@RunWith(AndroidJUnit4::class)
@Config(application = MockApp::class)
class RegisterFragmentTest: BaseTest() {

    @MockK lateinit var storage: IStorage
    @MockK lateinit var nav: INav

    lateinit var scenario: FragmentScenario<RegisterFragment>

    @Before override fun setUp() {
        super.setUp()
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_StatefulBread) {
            RegisterFragment().apply { registerVM = RegisterVM(storage, nav) }
        }
    }

    @After override fun tearDown() {
        super.tearDown()
        scenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test fun on_error() {
        scenario.onFragment {

            it.onError(Exception())
            Assert.assertEquals(it.binding.nameEditText.error, null)
            Assert.assertEquals(it.binding.emailEditText.error, null)
            Assert.assertEquals(it.binding.zipCodeEditText.error, null)
            Assert.assertEquals(it.binding.passwordEditText.error, null)


            it.onError(NameBlankEx())
            Assert.assertEquals(it.binding.nameEditText.error, NameBlankEx().message)

            it.onError(EmailBlankEx())
            Assert.assertEquals(it.binding.emailEditText.error, EmailBlankEx().message)

            it.onError(EmailInvalidEx())
            Assert.assertEquals(it.binding.emailEditText.error, EmailInvalidEx().message)

            it.onError(EmailExistsEx())
            Assert.assertEquals(it.binding.emailEditText.error, EmailExistsEx().message)

            it.onError(ZipCodeBlankEx())
            Assert.assertEquals(it.binding.zipCodeEditText.error, ZipCodeBlankEx().message)

            it.onError(PasswordBlankEx())
            Assert.assertEquals(it.binding.passwordEditText.error, PasswordBlankEx().message)

        }
    }

    @Test fun on_nav_to_login() {
        every { nav.toLogin() } returns Completable.complete()

        scenario.onFragment {
            it.binding.backButton.performClick()
            verify(exactly = 1) { nav.toLogin() }

        }
    }

    @Test fun on_data() {
        scenario.onFragment {

            it.binding.nameEditText.setText("Matthew")
            it.binding.emailEditText.setText("mshannon93@gmail.com")
            it.binding.zipCodeEditText.setText("53072")
            it.binding.passwordEditText.setText("abc123")
            it.getData().also { data ->
                Assert.assertEquals(data["age"], "0")
                Assert.assertEquals(data["name"], "Matthew")
                Assert.assertEquals(data["email"], "mshannon93@gmail.com")
                Assert.assertEquals(data["zipCode"], "53072")
                Assert.assertEquals(data["password"], "abc123")

            }

            it.binding.nameEditText.setText("")
            it.binding.emailEditText.setText("")
            it.binding.zipCodeEditText.setText("")
            it.binding.passwordEditText.setText("")
            it.getData().also { data ->
                Assert.assertEquals(data["age"], "0")
                Assert.assertEquals(data["name"], "")
                Assert.assertEquals(data["email"], "")
                Assert.assertEquals(data["zipCode"], "")
                Assert.assertEquals(data["password"], "")

            }
        }
    }

    @Test fun on_submit() {
        val userRepo: UserDao = mockk<UserDao>().also { every { storage.userRepo() } returns Single.just(it) }
        every { userRepo.findByCredentials(any(), any()) } returns Maybe.just(User.empty())
        every { userRepo.insert(any()) } returns Completable.complete()
        every { nav.toLogin() } returns Completable.complete()

        scenario.onFragment {
            it.binding.nameEditText.setText("Matthew")
            it.binding.emailEditText.setText("mshannon93@gmail.com")
            it.binding.zipCodeEditText.setText("53072")
            it.binding.passwordEditText.setText("abc123")

            it.onSubmit(it.getData())
            verify(exactly = 1) { nav.toLogin() }

            it.binding.registerButton.performClick()
            verify(exactly = 2) { nav.toLogin() }
        }
    }

}