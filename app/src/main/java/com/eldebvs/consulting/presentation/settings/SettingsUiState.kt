package com.eldebvs.consulting.presentation.settings


data class SettingsUiState(
    val email: String? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val password: String? = null,
    val showUploadPhotoDialog: Boolean = false,
    val requestReadStoragePermission: Boolean = false,
    val requestCameraPermission: Boolean = false
)
