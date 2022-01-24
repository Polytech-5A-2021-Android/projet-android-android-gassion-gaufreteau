package com.polytech.projet_android_iot.dao

import androidx.room.*
import com.polytech.projet_android_iot.model.PresetsIOT

@Dao
interface PresetsIOTDao {

    @Insert
    fun insert(preset: PresetsIOT): Long

    @Delete
    fun delete(preset: PresetsIOT)

    @Update
    fun update(preset: PresetsIOT)

    @Query("SELECT * from presetsIOT WHERE id = :key")
    fun get(key: Long): PresetsIOT?

    @Query("SELECT * from presetsIOT WHERE bid =:boardid")
    fun getPresets(boardid: Long): List<PresetsIOT>?

    @Query("SELECT * from presetsIOT WHERE name = :key AND bid =:boardid")
    fun getByName(key: String, boardid: Long): PresetsIOT?
}