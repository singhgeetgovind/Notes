package com.singhgeetgovind.notes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.singhgeetgovind.notes.dao.NotesDao
import com.singhgeetgovind.notes.database.Migration.DataBaseVersion
import com.singhgeetgovind.notes.model.Notes
import com.singhgeetgovind.notes.utils.OwnConvertor

@Database(entities = [Notes::class] , version = DataBaseVersion, exportSchema = true)
@TypeConverters(OwnConvertor::class)
abstract class NotesDatabase : RoomDatabase()
{
    abstract fun getDaoInstance() : NotesDao

}