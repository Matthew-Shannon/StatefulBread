package com.matthew.statefulbread.repo

import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.matthew.statefulbread.core.BaseTest
import com.matthew.statefulbread.repo.model.UserDao
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class StorageTest: BaseTest() {

    private lateinit var appDatabase: Temp
    private lateinit var storage: Storage

    @Before override fun setUp() {
        super.setUp()
        appDatabase = spyk()
        storage = Storage(appDatabase)
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
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
        storage.userRepo().test()
            .assertNoErrors()
    }

}

class Temp: AppDatabase() {
    override fun userRepo(): UserDao = mockk()
    override fun createOpenHelper(config: DatabaseConfiguration?): SupportSQLiteOpenHelper = mockk()
    override fun createInvalidationTracker(): InvalidationTracker = mockk()
    override fun clearAllTables() { }
}