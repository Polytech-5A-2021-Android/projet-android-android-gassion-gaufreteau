package com.polytech.projet_android_iot.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.polytech.projet_android_iot.dao.UserIOTDao
import com.polytech.projet_android_iot.model.UserIOT

@Database(entities = [UserIOT::class], version = 4, exportSchema = false)
abstract class DatabaseIotUser : RoomDatabase() {
    abstract val userIOTDao: UserIOTDao
    companion object {

        @Volatile
        private var INSTANCE: DatabaseIotUser? = null

        fun getInstance(context: Context): DatabaseIotUser {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseIotUser::class.java,
                        "db_user_iot"
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