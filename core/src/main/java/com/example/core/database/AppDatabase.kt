package com.example.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.database.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao
}