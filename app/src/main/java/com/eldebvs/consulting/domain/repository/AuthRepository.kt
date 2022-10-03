package com.eldebvs.consulting.domain.repository

import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerUser(email: String, password: String): Flow<Response<Boolean>>
    suspend fun signOutUser(): Flow<Response<Boolean>>
    suspend fun signInUser(email: String, password: String): Flow<Response<Boolean>>
    suspend fun getFirebaseAuthState(): Flow<Boolean>
    suspend fun resendVerificationEmail(email: String, password: String): Flow<Response<Boolean>>
    suspend fun getUserDetails(): Flow<Response<User?>>
    suspend fun editUserDetails(name: String?, phone: String?): Flow<Response<Boolean>>
    suspend fun resetUserPassword(): Flow<Response<Boolean>>
    suspend fun editUserEmail(email: String, password: String): Flow<Response<Boolean>>
}