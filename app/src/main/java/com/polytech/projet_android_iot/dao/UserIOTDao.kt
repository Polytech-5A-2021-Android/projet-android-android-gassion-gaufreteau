package com.polytech.projet_android_iot.dao

import androidx.room.*
import com.polytech.projet_android_iot.model.UserIOT

@Dao
interface UserIOTDao {
    @Insert
    fun insert(user: UserIOT): Long

    @Delete
    fun delete(user: UserIOT)

    @Update
    fun update(user: UserIOT)

    @Query("SELECT * from userIOT WHERE id = :key")
    fun get(key: Long): UserIOT?

    @Query("SELECT * from userIOT WHERE login = :login")
    fun getByLogin(login: String): UserIOT?

    @Query("SELECT * from userIOT WHERE login = :login AND password =:pwd")
    fun getByLoginAndPwd(login: String?, pwd: String?): UserIOT?

    @Query("SELECT * FROM userIOT")
    fun getUsers(): List<UserIOT>?

    @Query("SELECT * FROM userIOT ORDER BY id DESC LIMIT 1")
    fun getLastUser(): UserIOT?

}