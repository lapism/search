package com.lapism.search.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lapism.search.BuildConfig


@Database(entities = [Search::class], version = BuildConfig.VERSION_CODE, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // *********************************************************************************************v
    abstract fun searchDao(): SearchDao

    // *********************************************************************************************
    override fun close() {
        super.close()
        sDb?.close()
        sDb = null
    }

    // *********************************************************************************************
    companion object {

        private var sDb: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase? {
            if (sDb == null) {
                val c = context.applicationContext
                sDb = Room.databaseBuilder(c, AppDatabase::class.java, "searchDatabase").build()
            }
            return sDb
        }

    }

}
