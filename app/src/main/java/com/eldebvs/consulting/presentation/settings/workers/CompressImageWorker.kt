package com.eldebvs.consulting.presentation.settings.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.eldebvs.consulting.util.Constants.KEY_IMAGE_URI
import com.eldebvs.consulting.util.Constants.WORKER_OUTPUT


const val COMPRESS_TAG = "CompressWorker"

class CompressImageWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        return try {
            if (TextUtils.isEmpty(resourceUri)){
                Log.e(COMPRESS_TAG, "invalid input uri")
                throw IllegalArgumentException("invalid input uri")
            }
            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            val output = compressBitmap(picture)
            val outputUri = writeBitmapToFile(appContext,output).toString()
            val outputData = workDataOf(WORKER_OUTPUT to outputUri)
            Result.success(outputData)
        } catch (t: Throwable) {
            Log.e(COMPRESS_TAG, "Error compressing image: ${t.message}" )
            Result.Failure()
        }
    }
}