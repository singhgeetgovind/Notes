package com.singhgeetgovind.notes.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*


@Entity(indices = [Index(value = ["IntentId"], unique = true)])
@Parcelize
data class Notes(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID") val id: Int,
    @ColumnInfo(name = "Title") val title: String,
    @ColumnInfo(name = "Description") val description: String,
    @ColumnInfo(name = "NotesPriority") val hasPriority: Int,
    @ColumnInfo(name = "DateCreated") val createdDate: Date,
    @ColumnInfo(name = "EventDate") val eventDate: Long?,
    @ColumnInfo(name = "IntentId") val intentId: Int?=0, //Id helps to cancel the alarm while deleting ❌
    @ColumnInfo(name = "EventDone") val eventDone: Int?=0, //Mark event is completed ✅
) : BaseNotes,Parcelable

data class EmptyNotes(val id:Int,val message:String) : BaseNotes

interface BaseNotes
