package com.example.notes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notes.dao.NotesDao
import com.example.notes.model.Notes
import com.example.notes.utils.OwnConvertor

@Database(entities = [Notes::class] , version = 2, exportSchema = true)
@TypeConverters(OwnConvertor::class)
abstract class NotesDatabase : RoomDatabase()
{
    abstract fun getDaoInstance() : NotesDao

//    companion object{
//        private val migration_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("Alter table employee add column hasPriority INTEGER NOT NULL DEFAULT(0) ")  // This is for migration
//            }
//        }
//        @Volatile
//        private var employeeDatabase : EmployeeDatabase? = null
//
//        fun getDatabase(context : Context ) : EmployeeDatabase {
//            return employeeDatabase ?: synchronized(this){
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    EmployeeDatabase::class.java,
//                    "employee_database")
////                    .addMigrations(migration_1_2)  //Migrate
//                    .build()
//                employeeDatabase = instance
//
//                instance
//            }
//        }
//    }
}