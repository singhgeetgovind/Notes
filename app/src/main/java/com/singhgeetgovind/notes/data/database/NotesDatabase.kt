package com.singhgeetgovind.notes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.singhgeetgovind.notes.data.dao.NotesDao
import com.singhgeetgovind.notes.data.database.Migration.DataBaseVersion
import com.singhgeetgovind.notes.data.model.Notes
import com.singhgeetgovind.notes.utils.OwnConvertor

@Database(entities = [Notes::class] , version = DataBaseVersion, exportSchema = false/*true*/)
@TypeConverters(OwnConvertor::class)
abstract class NotesDatabase : RoomDatabase()
{
    abstract fun getDaoInstance() : NotesDao

}