package com.example.mvvmdemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvmdemo.presentation.home.AlbumModel

@Database(
    entities = [AlbumModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class MvvmDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao

    companion object {
        @Volatile
        private var INSTANCE: MvvmDatabase? = null
        private val LOCK = Any()

        fun getDatabase(context: Context): MvvmDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MvvmDatabase::class.java,
                    "mvvm_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}