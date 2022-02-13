package com.matthew.statefulbread.repo.model

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "age") val age: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "zipCode") val zipCode: String,
    @ColumnInfo(name = "password") val password: String,
) {

    companion object {

        fun empty(): User = User(
            id = 0,
            age = 0,
            name = "",
            email = "",
            zipCode = "",
            password = ""
        )

        fun def(data: Map<String,String>): User = User(
            id = 0,
            age = data.getOrDefault("age", "0").toInt(),
            name = data.getOrDefault("name", ""),
            email = data.getOrDefault("email", ""),
            zipCode = data.getOrDefault("zipCode", ""),
            password = data.getOrDefault("password", "")
        )

        fun explode(user: User): Map<String,String> = mapOf(
            "id" to "${user.id}",
            "age" to "${user.age}",
            "name" to user.name,
            "email" to user.email,
            "zipCode" to user.zipCode,
            "password" to user.password
        )
    }

}

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): Single<List<User>>

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    fun findByEmail(email: String): Maybe<User>

    @Query("SELECT * FROM user WHERE email LIKE :email AND password LIKE :password LIMIT 1")
    fun findByCredentials(email: String, password: String): Maybe<User>

    @Insert
    fun insert(user: User): Completable

    @Delete
    fun delete(user: User): Completable

}




