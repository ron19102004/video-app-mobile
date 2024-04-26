package com.video.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.video.app.R
import kotlin.random.Random

sealed class NotificationService {
    data object ShowMessage : NotificationService() {
        fun show(context: Context, contentTitle: String, contentText: String) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel =
                    NotificationChannel(
                        "ntfId",
                        "Notification",
                        importance
                    ).apply {
                        description = ""
                    }
                // Register the channel with the system.
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
                val notification = NotificationCompat.Builder(context, "ntfId")
                    .setSmallIcon(R.drawable.video_logo)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .build()
                notificationManager.notify(Random.nextInt(), notification)
            }
        }
    }
}