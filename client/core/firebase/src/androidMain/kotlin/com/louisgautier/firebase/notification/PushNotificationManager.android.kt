package com.louisgautier.firebase.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.louisgautier.utils.context.ContextWrapper

actual class PushNotificationManager(
    private val contextWrapper: ContextWrapper,
) {

    init {
        //Create Channel if needed
        //createNotificationChannel()
    }

    @SuppressLint("MissingPermission")
    actual fun sendNotification(data: PushNotificationData) {
        val builder = NotificationCompat.Builder(contextWrapper.context, "CHANNEL_ID")
//            .setSmallIcon() TODO(Fix): Crash if no icon
            .setContentTitle(data.title)
            .setContentText(data.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set priority for older Android versions
            .setAutoCancel(true) // Automatically removes the notification when the user taps it

        with(NotificationManagerCompat.from(contextWrapper.context)) {
            // notificationId is a unique int for each notification that you must define
            notify(data.hashCode(), builder.build())
        }
    }

    @Suppress("unused")
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel Name"
            val descriptionText = "My Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                contextWrapper.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}