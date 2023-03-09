package com.eldebvs.consulting.domain.repository

import com.eldebvs.consulting.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun registerUser(email: String, password: String): Flow<Response<Boolean>>
    fun signOutUser(): Flow<Response<Boolean>>
    fun signInUser(email: String, password: String): Flow<Response<Boolean>>
    fun getFirebaseAuthState(): Flow<Boolean>
    fun resendVerificationEmail(email: String, password: String): Flow<Response<Boolean>>
}