package com.eldebvs.consulting.presentation.settings

import android.net.Uri

data class UserDetails(
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val password: String? = null,
    val profile_photo_uri: Uri? = null,
    val enableCompression: Boolean = false,
    val profile_photo_firebase_url: String? = null
) {
    fun isSettingFormValid(): Boolean {
        val checkDetailsChanged: Boolean = (!name.isNullOrEmpty() || !phone.isNullOrEmpty())
        return if (!email.isNullOrEmpty()) {
            !password.isNullOrEmpty()
        } else {
            checkDetailsChanged
        }
    }
}
