package com.example.notes.receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notes.R
import com.example.notes.ui.activity.MainActivity

class CustomBroadcastReceiver : BroadcastReceiver() {


    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        createNotification(context,intent)
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(context: Context?, intentArgs: Intent?){
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,5,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "MyNotification",
                "MyNotification",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.apply{
                canShowBadge()
                description = "This is First Notification" }
            val notificationManager =context?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(notificationChannel)

        }
        val title= intent.getStringExtra("Title")
        val description= intent.getStringExtra("Description")
        val notification = NotificationCompat.Builder(context!!, "MyNotification").run{
            setSmallIcon(R.drawable.ic_baseline_task_alt_24)
            setContentTitle(title)
            setContentText(description)
            setContentIntent(pendingIntent)
            setAutoCancel(true) }
        val manager = context.let { NotificationManagerCompat.from(it) }
        manager.notify(556, notification.build())

    }
}