package com.example.notes.services


import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import com.example.notes.receiver.CustomBroadcastReceiver


fun alarm(trigger: Boolean, activity: Activity?, title:String="", description: String="",getInMilliSecond:Long=0) {

    val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val broadcastIntent = Intent(activity, CustomBroadcastReceiver::class.java)
    broadcastIntent.putExtra("Title", title)
    broadcastIntent.putExtra("Description", description)
    val broadcastPendingIntent = PendingIntent.getBroadcast(
        activity,
        1,
        broadcastIntent,
        0
    )
    if (trigger) {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            getInMilliSecond,broadcastPendingIntent)
    }
    else {
        alarmManager.cancel(broadcastPendingIntent)
    }
}
