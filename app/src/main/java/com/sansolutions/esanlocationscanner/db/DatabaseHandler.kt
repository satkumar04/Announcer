package com.sansolutions.esanblescanner.db

import androidx.room.Database
import androidx.room.Room
import androidx.room. RoomDatabase
import android.content.Context
import com.sansolutions.esanlocationscanner.db.LocationDAO
import com.sansolutions.esanlocationscanner.db.LocationEntity

@Database(entities =arrayOf(LocationEntity::class), version = 1)
abstract class DatabaseHandler : RoomDatabase() {

    abstract fun locationDao(): LocationDAO

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHandler? = null

        fun getDatabase(context: Context): DatabaseHandler? {
            if (INSTANCE == null)
            {
                synchronized(DatabaseHandler::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder<DatabaseHandler>(
                            context.applicationContext,
                            DatabaseHandler::class.java!!, "LocationTest"
                        )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build()
                    }
                }
        }
        return INSTANCE
    }}}