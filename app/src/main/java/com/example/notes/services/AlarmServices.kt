package com.example.notes.services


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.notes.receiver.AlarmBroadcastReceiver
import com.example.notes.utils.Constants


fun alarm(trigger: Boolean, activity: Context?, title:String="", description: String="",getInMilliSecond:Long=0) {

    val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val broadcastIntent = Intent(activity, AlarmBroadcastReceiver::class.java)
    broadcastIntent.action = Constants.ALARM_ACTIONS
    broadcastIntent.putExtra("Title", title)
    broadcastIntent.putExtra("Description", description)
    val broadcastPendingIntent = PendingIntent.getBroadcast(
        activity,
        0,
        broadcastIntent,
        0
    )
    if (trigger) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                getInMilliSecond,broadcastPendingIntent)
        }else{
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                getInMilliSecond,broadcastPendingIntent)
        }
    }
    else {
        alarmManager.cancel(broadcastPendingIntent)
    }
}
