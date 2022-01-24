package com.polytech.projet_android_iot.dao

import androidx.room.*
import com.polytech.projet_android_iot.model.BoardIOT

@Dao
interface BoardIOTDao {

    @Insert
    fun insert(user: BoardIOT): Long

    @Delete
    fun delete(user: BoardIOT)

    @Update
    fun update(user: BoardIOT)

    @Query("SELECT * from boardIOT WHERE id = :key")
    fun get(key: Long): BoardIOT?

    @Query("SELECT * from boardIOT")
    fun getBoards(): List<BoardIOT>

    @Query("SELECT * from boardIOT WHERE name =:name")
    fun getBoardByName(name: String?): List<BoardIOT>
}