package com.singhgeetgovind.notes.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private const val TAG = "Utils"
    const val DATE_PATTERN = "EEE dd MMM,hh:mm a"
    @SuppressLint("SimpleDateFormat")
    fun getInMilliSecond(cal: Calendar, hour: Int, minute: Int): Long {
        with(cal){
            set(Calendar.HOUR, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND,0)
        }
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.SECOND,0)
        calendar.set(Calendar.MILLISECOND,0)
        Log.d(TAG, "getInMilliSecond: ${cal.timeInMillis} ${calendar.timeInMillis}")
        return if (cal.before(calendar)) {
            calendar.run{
                add(Calendar.DATE, 1)
                timeInMillis
            }
        }else cal.timeInMillis
    }

    fun formatDate(outputString: String,date:Long?): String {
        return date?.let { SimpleDateFormat(outputString, Locale.ENGLISH).format(Date(date)) } ?: ""
    }
}