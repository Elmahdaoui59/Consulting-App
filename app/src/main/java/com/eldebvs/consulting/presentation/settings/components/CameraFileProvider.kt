package com.eldebvs.consulting.presentation.settings.components

import  android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.eldebvs.consulting.R
import java.io.File

class CameraFileProvider : FileProvider(R.xml.filepaths) {
    companion object {
        var uri: Uri = "".toUri()
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images")
            directory.mkdirs()

            val file = createTempFile(
                "selected_image_",
                ".jpg",
                directory
            )
            val authority = context.packageName + ".fileprovider"
            uri = getUriForFile(
                context,
                authority,
                file,
            )
            return uri
        }
    }
}