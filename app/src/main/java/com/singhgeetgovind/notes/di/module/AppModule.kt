package com.singhgeetgovind.notes.di.module


import android.app.Application
import androidx.room.Room
import com.singhgeetgovind.notes.dao.NotesDao
import com.singhgeetgovind.notes.database.Migration
import com.singhgeetgovind.notes.database.NotesDatabase
import com.singhgeetgovind.notes.repository.Repository
import com.singhgeetgovind.notes.retrofit.MyRetrofitBuilder
import com.singhgeetgovind.notes.retrofit.RetrofitApi
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
            .addMigrations(*Migration.DataBaseMigration)
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