package com.polytech.projet_android_iot.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.polytech.projet_android_iot.dao.UserDao
import com.polytech.projet_android_iot.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class DBase : RoomDatabase() {
    abstract val userDao: UserDao
    companion object {

        @Volatile
        private var INSTANCE: DBase? = null

        fun getInstance(context: Context): DBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DBase::class.java,
                        "my_database"
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