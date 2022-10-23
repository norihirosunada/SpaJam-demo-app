package com.norihiro.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Foods::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun FoodDao() : FoodDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "database",
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}