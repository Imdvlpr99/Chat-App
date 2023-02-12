package com.imdvlpr.chatapp.Shared.Extension

import android.content.res.Resources
import android.util.DisplayMetrics
import kotlin.math.roundToInt

fun convertDpToPx(dp: Int): Int {
    val metrics = Resources.getSystem().displayMetrics
    return (dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}