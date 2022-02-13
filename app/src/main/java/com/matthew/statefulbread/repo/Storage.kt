package com.matthew.statefulbread.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.matthew.statefulbread.repo.model.User
import com.matthew.statefulbread.repo.model.UserDao
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userRepo(): UserDao
}


interface IStorage {
    fun userRepo(): Single<UserDao>
    fun clear(): Completable
}


class Storage(private val _appDatabase: AppDatabase): IStorage {

    override fun userRepo(): Single<UserDao> = appDatabase()
        .map(AppDatabase::userRepo)

    override fun clear(): Completable = appDatabase()
        .flatMapCompletable { Completable.fromAction(it::clearAllTables) }

    private fun appDatabase(): Single<AppDatabase> = Single
        .just(_appDatabase)
        .observeOn(Schedulers.io())

    companion object {
        fun def(context: Context, name: String): IStorage = Storage(Room
            .databaseBuilder(context, AppDatabase::class.java, name)
            .build()
        )
    }

}