package com.polytech.projet_android_iot.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.polytech.projet_android_iot.dao.PresetsIOTDao
import com.polytech.projet_android_iot.model.PresetsIOT

@Database(entities = [PresetsIOT::class], version = 4, exportSchema = false)
abstract class DatabasePresets : RoomDatabase() {
    abstract val presetsIOTDao: PresetsIOTDao
    companion object {

        @Volatile
        private var INSTANCE: DatabasePresets? = null

        fun getInstance(context: Context): DatabasePresets {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabasePresets::class.java,
                        "db_presets_iot"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
