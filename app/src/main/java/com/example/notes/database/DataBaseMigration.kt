package com.example.notes.database

import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {
private const val TAG = "DataBaseMigration"

private val MIGRATION_2_3=object :Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            with(database){
                // Add the new column to the existing table
                execSQL("ALTER TABLE Notes ADD COLUMN IntentId INTEGER DEFAULT NULL");
                // Add a unique constraint to the new column
                execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_Notes_IntentId ON Notes (IntentId)");
            }
        }catch (e:Exception){
            Log.e(TAG, "version: ${database.version} ${e.message}")
        }
    }
}
private val MIGRATION_3_4=object :Migration(3,4){
    override fun migrate(database: SupportSQLiteDatabase) {
        try {
            with(database){
                // Update the column to the existing table
                execSQL("CREATE TABLE IF NOT EXISTS Notes1 (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Title TEXT NOT NULL, Description TEXT NOT NULL,NotesPriority INTEGER NOT NULL,DateCreated INTEGER NOT NULL,EventDate INTEGER DEFAULT null,IntentId INTEGER UNIQUE DEFAULT null)");
                execSQL("INSERT INTO Notes1  SELECT ID,Title ,Description,NotesPriority ,DateCreated,EventDate ,IntentId from Notes");
                execSQL("DROP TABLE Notes");
                execSQL("ALTER TABLE Notes1 ADD COLUMN EventDone INTEGER DEFAULT null");
                execSQL("ALTER TABLE Notes1 RENAME TO Notes");
                execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_Notes_IntentId ON Notes (IntentId)");
            }
        }catch (e:Exception){
            Log.e(TAG, "version: ${database.version} ${e.message}")
        }
    }
}

    var DataBaseMigration:Array<Migration> = arrayOf(MIGRATION_2_3, MIGRATION_3_4)
    private set

    const val DataBaseVersion = 4

}
