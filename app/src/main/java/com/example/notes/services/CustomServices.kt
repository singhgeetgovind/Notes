package com.example.notes.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build

import android.widget.Toast
import androidx.core.app.JobIntentService.enqueueWork
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notes.R
import com.example.notes.receiver.CustomBroadcastReceiver
import com.example.notes.ui.activity.MainActivity
import kotlin.concurrent.thread

class CustomServices : JobService() {
    private val TAG = "CustomServices"
    fun eneque_work(context: Context,intent: Intent){
        enqueueWork(context,CustomServices::class.java,102,intent)
    }
    override fun onDestroy() {
        Toast.makeText(this,"Service Done",Toast.LENGTH_SHORT).show()
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        thread {
           notification()
        }
        jobFinished(params,false)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    private fun notification() {
        val intent = Intent(this,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,0)
        val broadcastIntent = Intent(this,CustomBroadcastReceiver::class.java)
        val broadcastPendingIntent = PendingIntent.getBroadcast(this,0,broadcastIntent,0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "MyNotification",
                "MyNotification",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = "This is First Notification"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

        }
        val notification = NotificationCompat.Builder(this, "MyNotification")
            .setSmallIcon(R.drawable.ic_baseline_task_alt_24)
            .setContentTitle("First Notification Title")
            .setColorized(true)
            .setContentText("First Notification Description")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_baseline_error_outline_24,"Ok",broadcastPendingIntent)
        val manager = NotificationManagerCompat.from(this)
        manager.notify(556, notification.build())

    }
}