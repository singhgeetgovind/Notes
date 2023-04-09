package com.example.notes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*


@Entity(indices = [Index(value = ["IntentId"], unique = true)])
data class Notes(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID") val id: Int,
    @ColumnInfo(name = "Title") val title: String,
    @ColumnInfo(name = "Description") val description: String,
    @ColumnInfo(name = "NotesPriority") val hasPriority: Int,
    @ColumnInfo(name = "DateCreated") val createdDate: Date,
    @ColumnInfo(name = "EventDate") val eventDate: Date,
    @ColumnInfo(name = "IntentId") val intentId: Long?=0, //Id helps to cancel the alarm while deleting
) : BaseNotes

data class EmptyNotes(val id:Int,val message:String) : BaseNotes

interface BaseNotes
