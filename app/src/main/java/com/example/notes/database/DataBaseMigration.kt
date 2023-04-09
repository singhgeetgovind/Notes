package com.example.notes.database

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private const val TAG = "DataBaseMigration"

val MIGRATION_2_3=object :Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            with(database){
                // Add the new column to the existing table
                database.execSQL("ALTER TABLE Notes ADD COLUMN IntentId INTEGER DEFAULT NULL");
                // Add a unique constraint to the new column
                database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_Notes_IntentId ON Notes (IntentId)");
            }
        }catch (e:Exception){
            Log.e(TAG, "version: ${database.version} ${e.message}")
        }
    }
}

var DataBaseMigration = arrayOf(MIGRATION_2_3)
private set
