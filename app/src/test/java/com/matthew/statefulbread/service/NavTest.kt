package com.matthew.statefulbread.service

import androidx.fragment.app.FragmentManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.core.MockActivity
import com.matthew.statefulbread.core.MockApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(application = MockApp::class)
class NavTest: BaseTest() {

    private lateinit var activity: MockActivity
    private lateinit var manager: FragmentManager
    private lateinit var nav: Nav

    @Before override fun setUp() {
        super.setUp()
        activity = buildActivity(MockActivity::class.java).setup().get()
        manager = activity.supportFragmentManager
        nav = Nav(activity, manager, 420)//R.id.splash_container)
    }

    // TODO do better
    @Test fun get_current_title() {
        nav.getCurrentTitle().test()
            .assertNoErrors().dispose()
    }

    // TODO do better
    @Test fun launch_fragment() {
        nav.toRegister().test()
            .assertNoErrors().dispose()

        nav.toLogin().test()
            .assertNoErrors().dispose()

        nav.toHome().test()
            .assertNoErrors().dispose()

        nav.toSearch().test()
            .assertNoErrors().dispose()

        nav.toCategories().test()
            .assertNoErrors().dispose()

        nav.toFavorites().test()
            .assertNoErrors().dispose()

        nav.toSettings().test()
            .assertNoErrors().dispose()
    }

    // TODO do better
    @Test fun launch_activity() {

        nav.toSplash().test()
            .assertNoErrors().dispose()

        nav.toMain().test()
            .assertNoErrors().dispose()
    }

}