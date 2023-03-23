package com.example.notes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID") val id: Int,
    @ColumnInfo(name = "Title") val title: String,
    @ColumnInfo(name = "Description") val description: String,
    @ColumnInfo(name = "NotesPriority") val hasPriority: Int,
    @ColumnInfo(name = "DateCreated") val date: Date,
    @ColumnInfo(name = "EventDate") val eventDate: Date,
) : BaseNotes

data class EmptyNotes(val id:Int,val message:String) : BaseNotes

interface BaseNotes
