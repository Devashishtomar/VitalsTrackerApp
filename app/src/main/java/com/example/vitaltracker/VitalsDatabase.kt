package com.example.vitaltracker

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room

@Database(entities = [Vitals::class], version = 1, exportSchema = false)
abstract class VitalsDatabase : RoomDatabase() {

    abstract fun vitalsDao(): VitalsDao

    companion object {
        @Volatile
        private var INSTANCE: VitalsDatabase? = null

        fun getDatabase(context: Context): VitalsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitalsDatabase::class.java,
                    "vitals_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}