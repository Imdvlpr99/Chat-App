package com.imdvlpr.chatapp.Shared.Extension

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Parcelable
import android.util.Base64
import android.util.DisplayMetrics
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun convertDpToPx(dp: Int): Int {
    val metrics = Resources.getSystem().displayMetrics
    return (dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun encodedImage(bitmap: Bitmap): String {
    val previewWidth = 300
    val previewHeight = bitmap.height * previewWidth / bitmap.width
    val previewBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
    val byteArrayOutputStream = ByteArrayOutputStream()
    previewBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byte = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byte, Base64.DEFAULT)
}

fun decodeImage(encodedImage: String): Bitmap {
    val bytes = Base64.decode(encodedImage, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun getReadableDate(date: Date): String {
    return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(date)
}

fun <T : Parcelable?> Intent.getParcelable(key: String, className: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= 33)
        this.getParcelableExtra(key, className)
    else
        this.getParcelableExtra(key)
}