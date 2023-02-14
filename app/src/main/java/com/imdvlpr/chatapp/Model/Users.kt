package com.imdvlpr.chatapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    var name: String = "",
    var email: String = "",
    var image: String = "",
    var token: String = "",
    var id: String = ""
): Parcelable
