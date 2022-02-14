package com.matthew.statefulbread.service

import com.matthew.statefulbread.core.BaseTest
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class StorageTest: BaseTest() {

    @MockK lateinit var appDatabase: AppDatabase_Impl
    private lateinit var storage: Storage

    @Before override fun setUp() {
        super.setUp()
        storage = Storage(appDatabase)
    }

    @Test fun app_database() {
        storage.appDatabase().test()
            .assertValue(appDatabase).dispose()
    }

    @Test fun clear() {
        storage.clear().test()
            .assertNoErrors().dispose()
            .run { verify(exactly = 1) { appDatabase.clearAllTables() } }
    }

    @Test fun userRepo() {
        every { appDatabase.userRepo() } returns mockk()
        storage.userRepo().test()
            .assertNoErrors()
    }

}