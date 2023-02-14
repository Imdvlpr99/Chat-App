package com.imdvlpr.chatapp.Shared.Extension

object Constants {

    interface KEY_USERS {
        companion object {
            const val KEY_COLLECTION_USERS = "users"
            const val KEY_NAME = "name"
            const val KEY_EMAIL = "email"
            const val KEY_PASSWORD = "password"
            const val KEY_PREFERENCE_NAME = "chatAppPreference"
            const val KEY_IS_SIGNED_IN = "isSignedIn"
            const val KEY_USER_ID = "userId"
            const val KEY_IMAGE = "image"
            const val KEY_FCM_TOKEN = "fcmToken"
            const val KEY_PHONE = "phone"
        }
    }

    interface KEY_CHATS {
        companion object {
            const val KEY_COLLECTION_CHAT = "chats"
            const val KEY_SENDER_ID = "senderId"
            const val KEY_RECEIVER_ID = "receiverId"
            const val KEY_MESSAGE = "message"
            const val KEY_TIMESTAMP = "timestamp"
            const val KEY_IS_READ = "isRead"
        }
    }
}