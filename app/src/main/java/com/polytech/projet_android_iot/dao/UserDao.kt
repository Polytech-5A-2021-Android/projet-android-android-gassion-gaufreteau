package com.polytech.projet_android_iot.dao

import androidx.room.*
import com.polytech.projet_android_iot.model.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User): Long

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * from user WHERE id = :key")
    fun get(key: Long): User?

    @Query("SELECT * FROM user")
    fun getUsers(): List<User>?

    @Query("SELECT * FROM user ORDER BY id DESC LIMIT 1")
    fun getLastUser(): User?

}

