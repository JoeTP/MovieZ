package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.database.AppDatabase
import com.example.core.database.MoviesDao
import com.example.core.utils.constants.AppStrings.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, DATABASE_NAME).build()

    @Provides
    fun provideMovieDao(db: AppDatabase): MoviesDao = db.movieDao()

}