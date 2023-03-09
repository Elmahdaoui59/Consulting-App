package com.eldebvs.consulting.domain.use_case.settings_use_case

data class SettingsUsesCases(
    val editUserEmail: EditUserEmail,
    val editUserDetails: EditUserDetails,
    val getUserDetails: GetUserDetails,
    val resetUserPassword: ResetUserPassword,
    val uploadImage: UploadImage,
    val compressAndUploadImage: CompressAndUploadImage
)