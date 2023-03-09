package com.eldebvs.consulting.domain.use_case.settings_use_case

import android.net.Uri
import com.eldebvs.consulting.domain.repository.SettingsRepository

class CompressAndUploadImage(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(imageUri: Uri) = settingsRepository.compressAndUploadImage(imageUri)
}