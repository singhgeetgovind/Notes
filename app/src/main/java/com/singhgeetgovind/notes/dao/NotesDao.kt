package com.singhgeetgovind.notes.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.singhgeetgovind.notes.model.Notes

@Dao
interface NotesDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDetails(notes: Notes)

    @Query("SELECT * FROM Notes ORDER BY DateCreated DESC")
    fun getAllDetails() : LiveData<List<Notes>>

    @Query("DELETE FROM Notes WHERE ID in (:id)")
    suspend fun deleteById(id: List<Int>)

    @Query("DELETE FROM Notes")
    suspend fun deleteAll()

    @Update
    suspend fun updateData(notes: Notes)

    @Query("UPDATE Notes set EventDone = 1 where IntentId=:intentId")
    suspend fun updateEventTrigger(intentId:Int)

    @Query("SELECT * FROM NOTES WHERE Title LIKE '%' || :searchQuery || '%' OR Description LIKE '%' || :searchQuery || '%'")
    fun searchQueryList(searchQuery:String) : LiveData<List<Notes>>
}