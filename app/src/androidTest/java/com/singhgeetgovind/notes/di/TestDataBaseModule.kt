package com.singhgeetgovind.notes.di

import android.content.Context
import androidx.room.Room
import com.singhgeetgovind.notes.dao.NotesDao
import com.singhgeetgovind.notes.database.NotesDatabase
import com.singhgeetgovind.notes.di.module.DatabaseModule
import com.singhgeetgovind.notes.repository.Repository
import com.singhgeetgovind.notes.retrofit.MyRetrofitBuilder
import com.singhgeetgovind.notes.retrofit.RetrofitApi
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@TestInstallIn(components = [SingletonComponent::class],
                replaces = [DatabaseModule::class])
@Module
class TestDataBaseModule  {
    @Provides
    @Singleton
    fun provideNotesDatabase(@ApplicationContext context : Context):NotesDatabase{
        return Room.inMemoryDatabaseBuilder(
           context,
            NotesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
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