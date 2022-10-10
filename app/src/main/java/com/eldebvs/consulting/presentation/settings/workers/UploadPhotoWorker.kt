package com.eldebvs.consulting.presentation.settings.workers

import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker.Result.failure
import androidx.work.ListenableWorker.Result.success
import androidx.work.WorkerParameters
import com.eldebvs.consulting.R
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.use_case.settings_use_case.SettingsUsesCases
import com.eldebvs.consulting.util.Constants.UPLOAD_NOTIFICATION_CHANNEL_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.random.Random

@HiltWorker
class UploadPhotoWorker @AssistedInject constructor(
    private val settingsUsesCases: SettingsUsesCases,
    @Assisted private val ctx: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        showNotification()

        try {
            val uri = inputData.getString(WorkerKeys.COMPRESSED_PHOTO_URI)?.toUri()?.toFile()?.absolutePath
            val bytes = withContext(Dispatchers.IO) {
                Files.readAllBytes(Paths.get(uri))
            }
            val response = bytes.let { settingsUsesCases.uploadImage(it) }
            if (response is Response.Success) {
                return success()
            }
            if (response is Response.Failure) {
                return failure()
            }
        } catch (e: java.lang.Exception) {
            Log.e(WorkerKeys.UPLOAD_ERROR, "Error uploading photo: ${e.message}")
            return failure()
        }
        return failure()
    }


    private suspend fun showNotification() {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(ctx, UPLOAD_NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_cloud_upload_24)
                    .setContentText("uploading...")
                    .setContentTitle("profile photo upload in progress")
                    .build()
            )
        )
    }
}