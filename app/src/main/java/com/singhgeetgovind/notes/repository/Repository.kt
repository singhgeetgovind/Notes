package com.singhgeetgovind.notes.repository

import androidx.lifecycle.LiveData
import com.singhgeetgovind.notes.dao.NotesDao
import com.singhgeetgovind.notes.model.Notes
import javax.inject.Inject


class Repository @Inject constructor(private val notesDao: NotesDao/*, private val retrofitApi: RetrofitApi*/)  {

    fun getData(): LiveData<List<Notes>> = notesDao.getAllDetails()

    suspend fun addData(notes: Notes){
        notesDao.addDetails(notes)
    }
    suspend fun deleteData(id : List<Int>){
        notesDao.deleteById(id)
    }
    suspend fun updateData(notes: Notes){
        notesDao.updateData(notes)
    }
    suspend fun deleteAll(){
        notesDao.deleteAll()
    }
    suspend fun updateEventTrigger(intentId:Int){
        notesDao.updateEventTrigger(intentId)
    }
    fun searchQueryList(searchQuery: String): LiveData<List<Notes>> = notesDao.searchQueryList(searchQuery)
}