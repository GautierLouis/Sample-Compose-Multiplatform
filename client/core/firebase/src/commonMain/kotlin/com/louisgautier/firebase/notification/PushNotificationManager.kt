package com.louisgautier.firebase.notification

expect class PushNotificationManager {
    /** Push a notification to notification (from service). */
    fun sendNotification(data: PushNotificationData)
}