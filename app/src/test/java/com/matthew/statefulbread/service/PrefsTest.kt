package com.matthew.statefulbread.service

import android.content.SharedPreferences
import com.matthew.statefulbread.core.BaseTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PrefsTest: BaseTest() {

    @MockK lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefs: Prefs

    @Before override fun setUp() {
        super.setUp()
        prefs = Prefs(sharedPreferences)
    }

    @Test fun on_set_age() {
        val editor: SharedPreferences.Editor = mockk()
        every { sharedPreferences.edit() } returns editor
        every { editor.putInt(any(), any()) } returns editor
        every { editor.apply() } returns Unit


        prefs.setAge(1).test().dispose()
            .also { verify(exactly = 1) { editor.putInt(any(), any()) } }
    }

    @Test fun on_get_age() {
        every { sharedPreferences.getInt(any(), any()) } returns 1

        prefs.getAge().test().dispose()
            .also { verify(exactly = 1) { sharedPreferences.getInt(any(), any()) } }
    }

    @Test fun on_set_owner_email() {
        val editor: SharedPreferences.Editor = mockk()
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } returns Unit

        prefs.setOwnerEmail("asdf").test().dispose()
            .also { verify(exactly = 1) { editor.putString(any(), any()) } }
    }

    @Test fun on_get_owner_email() {
        every { sharedPreferences.getString(any(), any()) } returns "asdf"

        prefs.getOwnerEmail().test().dispose()
            .also { verify(exactly = 1) { sharedPreferences.getString(any(), any()) } }
    }

    @Test fun on_set_auth_status() {
        val editor: SharedPreferences.Editor = mockk()
        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.apply() } returns Unit

        prefs.setAuthStatus(true).test().dispose()
            .also { verify(exactly = 1) { editor.putBoolean(any(), any()) } }
    }

    @Test fun on_get_auth_status() {
        every { sharedPreferences.getBoolean(any(), any()) } returns true

        prefs.getAuthStatus().test().dispose()
            .also { verify(exactly = 1) { sharedPreferences.getBoolean(any(), any()) } }
    }

    @Test fun on_set_dark_mode() {
        val editor: SharedPreferences.Editor = mockk()
        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.apply() } returns Unit

        prefs.setDayNightMode(true).test().dispose()
            .also { verify(exactly = 1) { editor.putBoolean(any(), any()) } }
    }

    @Test fun on_get_dark_mode() {
        every { sharedPreferences.getBoolean(any(), any()) } returns true

        prefs.getDayNightMode().test().dispose()
            .also { verify(exactly = 1) { sharedPreferences.getBoolean(any(), any()) } }
    }

    @Test fun on_get_all() {
        every { sharedPreferences.all } returns null

        prefs.getAll().test().dispose()
            .also { verify(exactly = 1) { sharedPreferences.all } }
    }

    @Test fun on_clear() {
        val editor: SharedPreferences.Editor = mockk()
        every { sharedPreferences.edit() } returns editor
        every { editor.clear() } returns editor
        every { editor.apply() } returns Unit

        prefs.clear().test().dispose()
            .also { verify(exactly = 1) { editor.clear() } }
    }

}
