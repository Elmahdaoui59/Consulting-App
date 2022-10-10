package com.eldebvs.consulting.presentation.settings.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.CLEANUP_TAG
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.COMPRESS_TAG
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.OUTPUT_DIR
import kotlinx.coroutines.delay
import java.io.File

class CleanupWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {

        delay(1000)

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_DIR)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".jpg")) {
                            val deleted = entry.delete()
                            Log.i(CLEANUP_TAG, "Deleted $name - $deleted")
                        }
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e(COMPRESS_TAG, "Error CleaningUp image: ${e.message}")
            Result.failure()
        }
    }

}
