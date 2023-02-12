package com.imdvlpr.chatapp.Shared.Extension

import android.content.res.Resources
import android.graphics.Bitmap
import android.util.Base64
import android.util.DisplayMetrics
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

fun convertDpToPx(dp: Int): Int {
    val metrics = Resources.getSystem().displayMetrics
    return (dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun encodedImage(bitmap: Bitmap): String {
    val previewWidth: Int = 150
    val previewHeight = bitmap.height * previewWidth / bitmap.width
    val previewBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
    val byteArrayOutputStream = ByteArrayOutputStream()
    previewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byte = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byte, Base64.DEFAULT)
}