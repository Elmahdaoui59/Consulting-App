package com.eldebvs.consulting.domain.repository

import android.net.Uri
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.model.User
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun compressAndUploadImage(imageUri: Uri)
    fun getUserDetails(): Flow<Response<User?>>
    fun editUserDetails(name: String?, phone: String?): Flow<Response<Boolean>>
    fun editUserEmail(email: String, password: String): Flow<Response<Boolean>>
    fun resetUserPassword(): Flow<Response<Boolean>>
    suspend fun uploadImageToFirebase(mBytes: ByteArray): Response<Boolean>
}