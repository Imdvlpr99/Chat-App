package com.imdvlpr.chatapp.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class ChatMessage(
    var dateTime: Date = Date(),
    var data: ArrayList<Message> = ArrayList()
): Parcelable
