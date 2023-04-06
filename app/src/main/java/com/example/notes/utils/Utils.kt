package com.example.notes.utils

import android.annotation.SuppressLint
import java.util.*

object Utils {
    @SuppressLint("SimpleDateFormat")
    fun getInMilliSecond(date: Calendar, hour: Int, minute: Int): Long {
        date.set(Calendar.HOUR, hour)
        date.set(Calendar.MINUTE, minute)
        date.set(Calendar.SECOND, 0)

        if (date.before(Calendar.getInstance())) {
            date.add(Calendar.DATE, 1)
        }
        return date.timeInMillis
    }
}