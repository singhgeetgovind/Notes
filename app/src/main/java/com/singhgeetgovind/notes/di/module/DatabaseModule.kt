package com.singhgeetgovind.notes.di.module


import android.app.Application
import androidx.room.Room
import com.singhgeetgovind.notes.data.dao.NotesDao
import com.singhgeetgovind.notes.data.database.Migration
import com.singhgeetgovind.notes.data.database.NotesDatabase
import com.singhgeetgovind.notes.data.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): NotesDatabase =
        Room.databaseBuilder(
            application,
            NotesDatabase::class.java,
            "NotesDatabase"
        )
            .addMigrations(*Migration.DataBaseMigration)
            .build()

    @Singleton
    @Provides
    fun provideDao(notesDatabase: NotesDatabase) : NotesDao =
        notesDatabase.getDaoInstance()

    @Singleton
    @Provides
    fun provideRepository(notesDao: NotesDao/*, retrofitApi: RetrofitApi*/) : Repository =
        Repository(notesDao/*, retrofitApi*/)

}