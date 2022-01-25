package com.polytech.projet_android_iot.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.polytech.projet_android_iot.dao.BoardIOTDao
import com.polytech.projet_android_iot.model.BoardIOT


@Database(entities = [BoardIOT::class], version = 6, exportSchema = false)
abstract class DatabaseIotBoard : RoomDatabase() {
    abstract val boardIOTDao: BoardIOTDao
    companion object {

        @Volatile
        private var INSTANCE: DatabaseIotBoard? = null

        fun getInstance(context: Context): DatabaseIotBoard {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseIotBoard::class.java,
                        "db_board_iot"
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