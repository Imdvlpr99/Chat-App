package com.imdvlpr.chatapp.Shared.firebase

import android.util.Log
import androidx.annotation.NonNull
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(@NonNull token: String) {
        super.onNewToken(token)
        Log.d("FCM", "token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("FCM", "message: ${message.notification?.body}")
    }
}