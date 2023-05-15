package com.example.notes.receiver

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.notes.R
import com.example.notes.ui.activity.MainActivity
import com.example.notes.utils.Constants
import com.example.notes.worker.AlarmWorker
import java.util.*

class AlarmBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "AlarmBroadcastReceiver"
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action==Constants.ALARM_ACTIONS){
            createNotification(context, intent)
            val workManager = WorkManager.getInstance(context!!)
            val worker = OneTimeWorkRequestBuilder<AlarmWorker>()
                        .build()
        }
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(context: Context?, intent: Intent?){
        val newIntent = Intent(context,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val intentId= intent?.getIntExtra("IntentId",0) ?: 0
        val title= intent?.getStringExtra("Title")
        val description= intent?.getStringExtra("Description")
        Log.d(TAG, "createNotification: $intentId")
        val pendingIntent = PendingIntent.getActivity(context,intentId,newIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Constants.CHANNEL_ID_REMINDERS,
                Constants.CHANNEL_ID_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.apply{
                canShowBadge()
                this.description = description }
            val notificationManager =context?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(notificationChannel)

        }
        val notification = NotificationCompat.Builder(context!!, Constants.CHANNEL_ID_REMINDERS).run{
            setSmallIcon(R.drawable.ic_baseline_task_alt_24)
            setContentTitle(title)
            setContentText(description)
            setStyle(NotificationCompat.BigTextStyle())
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            setPriority(NotificationCompat.PRIORITY_HIGH)
        }
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(Random().nextInt(), notification.build())
        }
//        CoroutineScope(Dispatchers.IO).launch{ notesDao.updateEventTrigger(IntentId) }
    }
}