package com.eldebvs.consulting.presentation.settings.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.annotation.WorkerThread
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.COMPRESS_TAG
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.OUTPUT_DIR
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.OUTPUT_PATH
import com.eldebvs.consulting.util.Constants.MB
import com.eldebvs.consulting.util.Constants.MB_THRESHOLD
import java.io.*
import java.nio.file.Files
import java.util.*

@WorkerThread
fun compressBitmap(bitmap: Bitmap): ByteArray {
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
            return bytes
        }
    }
    if (bytes != null) {
        return bytes
    }else {
        throw Exception("error compressing image")
    }
}


@Throws(FileNotFoundException::class)
fun writeBytesToFile(ctx: Context, bytes: ByteArray): Uri {
        val name = String.format(OUTPUT_PATH, UUID.randomUUID().toString())
        val outputDir = File(ctx.filesDir, OUTPUT_DIR)
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, name)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            out.use { stream ->
                stream.write(bytes)
            }
        } catch (e: Exception){
            Log.d("from writeBytesToFile", e.message.toString())
        } finally {
            out?.let {
                try {
                    it.close()
                } catch (e: IOException) {
                    Log.d("from writeBytesToFile", e.message.toString())
                }
            }
        }

        return Uri.fromFile(outputFile)

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