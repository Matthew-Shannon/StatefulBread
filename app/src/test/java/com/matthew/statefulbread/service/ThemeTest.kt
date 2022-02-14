package com.matthew.statefulbread.service

import androidx.appcompat.app.AppCompatDelegate
import com.matthew.statefulbread.core.BaseTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import java.util.function.Consumer

class ThemeTest: BaseTest() {

    @MockK lateinit var prefs: IPrefs
    @MockK lateinit var onSet: Consumer<Int>
    private lateinit var theme: Theme

    @Before override fun setUp() {
        super.setUp()
        theme = Theme(prefs, onSet)
    }

    @Test fun set_day_night_mode() {
        theme.setDayNightMode(false).test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { onSet.accept(AppCompatDelegate.MODE_NIGHT_NO) } }

        theme.setDayNightMode(true).test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { onSet.accept(AppCompatDelegate.MODE_NIGHT_YES) } }
    }

    @Test fun init_day_night_mode() {
        every { prefs.getDayNightMode() } returns Single.just(false)
        theme.initDayNightMode().test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { onSet.accept(AppCompatDelegate.MODE_NIGHT_NO) } }

        every { prefs.getDayNightMode() } returns Single.just(true)
        theme.initDayNightMode().test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { onSet.accept(AppCompatDelegate.MODE_NIGHT_YES) } }
    }

    @Test fun toggle_day_night_mode() {
        every { prefs.setDayNightMode(any()) } returns Completable.complete()

        every { prefs.getDayNightMode() } returns Single.just(false)
        theme.toggleDayNightMode().test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { onSet.accept(AppCompatDelegate.MODE_NIGHT_YES) } }
            .run { verify(exactly = 1) { prefs.setDayNightMode(true) } }

        every { prefs.getDayNightMode() } returns Single.just(true)
        theme.toggleDayNightMode().test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { onSet.accept(AppCompatDelegate.MODE_NIGHT_NO) } }
            .run { verify(exactly = 1) { prefs.setDayNightMode(false) } }

    }

}
