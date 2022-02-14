package com.matthew.statefulbread.core

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import io.mockk.MockKAnnotations
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.robolectric.Robolectric.buildActivity
import org.robolectric.android.controller.ActivityController

open class BaseTest {
    @Rule @JvmField var schedulerRule = RxTrampolineSchedulerRule()
    @Before open fun setUp() { MockKAnnotations.init(this, relaxUnitFun = true) }
    @After open fun tearDown() {  }
}

class RoboActivityRule<T: Activity>(private val clazz: Class<T>): TestWatcher() {
    lateinit var controller: ActivityController<T>

    override fun starting(description: Description?) {
        super.starting(description)
        controller = buildActivity(clazz)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        controller.stop()
        controller.destroy()
    }
}

class RxTrampolineSchedulerRule : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    override fun finished(description: Description?) {
        super.finished(description)
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}


class MockApp: Application()

class MockActivity: AppCompatActivity()