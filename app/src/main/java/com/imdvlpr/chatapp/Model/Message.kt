package com.imdvlpr.chatapp.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Message(
    var senderId: String = "",
    var receiverId: String = "",
    var message: String = "",
    var dateTime: String = "",
    var isRead: Boolean = false,
    var dateObject: Date = Date()
): Parcelable
