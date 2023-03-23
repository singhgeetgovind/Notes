package com.example.notes.di.module


import android.app.Application

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notes.dao.NotesDao
import com.example.notes.database.NotesDatabase
import com.example.notes.repository.Repository
import com.example.notes.retrofit.MyRetrofitBuilder
import com.example.notes.retrofit.RetrofitApi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): NotesDatabase =
        Room.databaseBuilder(
            application,
            NotesDatabase::class.java,
            "NotesDatabase"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideDao(notesDatabase: NotesDatabase) : NotesDao =
        notesDatabase.getDaoInstance()

    @Singleton
    @Provides
    fun provideApiBuilder(retrofitBuilder: MyRetrofitBuilder) : RetrofitApi =
        retrofitBuilder.getRetrofitApi()

    @Singleton
    @Provides
    fun provideRepository(notesDao: NotesDao/*, retrofitApi: RetrofitApi*/) : Repository =
        Repository(notesDao/*, retrofitApi*/)

}