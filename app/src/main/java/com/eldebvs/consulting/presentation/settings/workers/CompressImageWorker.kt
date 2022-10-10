package com.eldebvs.consulting.presentation.settings.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.COMPRESS_TAG
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.KEY_IMAGE_URI
import com.eldebvs.consulting.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random


class CompressImageWorker(
    private val ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        showNotification()
        val appContext = applicationContext
        val resolver = appContext.contentResolver

        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        Log.d("compression input uri:", resourceUri.toString())
        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Log.e(COMPRESS_TAG, "invalid input uri")
                throw IllegalArgumentException("invalid input uri")
            }
            val picture =
                BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            val output = compressBitmap(picture)
            val uri = withContext(Dispatchers.IO) {
                writeBytesToFile(ctx, output)
            }
            val outputData = workDataOf(WorkerKeys.COMPRESSED_PHOTO_URI to uri.toString())
            Result.success(outputData)
        } catch (e: Exception) {
            Log.e(COMPRESS_TAG, "Error compressing image: ${e.message}")
            Result.Failure()
        }
    }

    private suspend fun showNotification() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(ctx, Constants.UPLOAD_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_archive_24)
                    .setContentText("Compressing...")
                    .setContentTitle("profile photo compression in progress")
                    .build()
            )
        )
    }
}