package com.eldebvs.consulting.presentation.settings.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.annotation.WorkerThread
import com.eldebvs.consulting.util.Constants.MB
import com.eldebvs.consulting.util.Constants.MB_THRESHOLD
import com.eldebvs.consulting.util.Constants.OUTPUT_PATH
import java.io.*
import java.util.*

@WorkerThread
fun compressBitmap(bitmap: Bitmap): Bitmap {
    var bytes: ByteArray? = null
    for (i in 1..100) {
        if (i == 100) {
            throw Exception("That image is too large")
        }
        bytes = getBytesFromBitmap(bitmap = bitmap, 100 / i)
        Log.d(
            COMPRESS_TAG,
            "compression in progress: megabytes: (" + (11 - i) + "0%)" + bytes.size / MB + "MB"
        )
        if ((bytes.size / MB) < MB_THRESHOLD) {
            return getBitmapFromByteArray(bytes)
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
@Throws(FileNotFoundException::class)
fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
    val name = String.format("compress-image-output-%s.png", UUID.randomUUID().toString())
    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
    if (!outputDir.exists()) {
        outputDir.mkdirs() // should succeed
    }
    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (ignore: IOException) {
            }

        }
    }
    return Uri.fromFile(outputFile)
}