package com.imdvlpr.chatapp.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatMessage(
    var dateTime: String = "",
    var data: ArrayList<Message> = ArrayList()
): Parcelable
