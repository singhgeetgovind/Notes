package com.example.notes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notes.dao.NotesDao
import com.example.notes.database.Migration.DataBaseVersion
import com.example.notes.model.Notes
import com.example.notes.utils.OwnConvertor

@Database(entities = [Notes::class] , version = DataBaseVersion, exportSchema = true)
@TypeConverters(OwnConvertor::class)
abstract class NotesDatabase : RoomDatabase()
{
    abstract fun getDaoInstance() : NotesDao

}