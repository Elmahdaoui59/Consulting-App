package com.eldebvs.consulting.presentation.settings.workers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import com.eldebvs.consulting.util.Constants.MB
import com.eldebvs.consulting.util.Constants.MB_THRESHOLD
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@WorkerThread
fun compressBitmap(bitmap: Bitmap): Bitmap {
    var bytes: ByteArray? = null
    for (i in 1..10) {
        if (i == 10) {
            throw Exception("That image is too large")
        }
        bytes = getBytesFromBitmap(bitmap = bitmap, 100 / i)
        Log.d(
            COMPRESS_TAG,
            "compression in progress: megabytes: (" + (11 - i) + "0%)" + bytes.size / MB + "MB"
        )
        if (bytes.size / MB < MB_THRESHOLD) {
            break
        }
    }
    if (bytes != null) {
        return getBitmapFromByteArray(bytes)
    }else {
        throw Exception("error compressing image")
    }
}

fun getBytesFromBitmap(bitmap: Bitmap, quality: Int): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
    return stream.toByteArray()
}

fun getBitmapFromByteArray(bytes: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}