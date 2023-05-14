package com.example.notes.utils

import androidx.room.TypeConverter
import java.util.*

class OwnConvertor {

    @TypeConverter
    fun dateToLong(date: Date?) : Long?{
        return date?.time
    }
    @TypeConverter
    fun longToDate(long: Long?) : Date{
        return Date(long?:0L)
    }

    @TypeConverter
    fun boolToNumber(boolean: Boolean) : Int{
        return when (boolean){
            true -> 1
            false -> 0
        }
    }
    fun numToBoolean(int: Int) :Boolean{
        return when (int){
            0 -> false
            else -> true
        }
    }
}