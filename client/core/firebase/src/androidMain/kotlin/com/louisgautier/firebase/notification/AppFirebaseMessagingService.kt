package com.louisgautier.firebase.notification

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.louisgautier.logger.AppLogger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppFirebaseMessagingService : KoinComponent, FirebaseMessagingService() {

    private val manager: PushNotificationManager by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //TODO(Task): Send token to server (+preference?)
        // Do in :auth
    }

    @SuppressLint("MissingPermission")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val data = remoteMessage.data

        AppLogger.d("Push Notification received : ${remoteMessage.messageId}")

        manager.sendNotification(PushNotificationData(title, body, data))

    }
}

