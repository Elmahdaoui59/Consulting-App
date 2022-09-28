package com.eldebvs.consulting.data.repository

import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override suspend fun registerUser(email: String, password: String): Flow<Response<Boolean>> =
        flow {
            try {
                emit(Response.Loading)
                auth.createUserWithEmailAndPassword(email, password).await().run {
                    auth.currentUser?.sendEmailVerification()?.await().run {
                         emit(Response.Success(true))
                    }
                }
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }

        }

    override suspend fun signOutUser(): Flow<Response<Boolean>> =
        flow {
            try {
                emit(Response.Loading)
                auth.currentUser?.let {
                    auth.signOut()
                    emit(Response.Success(true))
                }
            } catch (e: java.lang.Exception) {
                emit(Response.Failure(e))
            }
        }

    override suspend fun signInUser(email: String, password: String): Flow<Response<Boolean>> =
        flow {
            try {
                emit(Response.Loading)
                auth.signInWithEmailAndPassword(email, password).await().run {
                    this?.let {
                        emit(Response.Success(true))
                    }
                }
            } catch (e:Exception) {
                emit(Response.Failure(e))
            }
        }

    override fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { authe ->
            trySend(
                authe.currentUser != null && (authe.currentUser?.isEmailVerified == true)
            )
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun resendVerificationEmail(
        email: String,
        password: String
    ): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            val credential: AuthCredential = EmailAuthProvider.getCredential(email, password)
            auth.signInWithCredential(credential).await().run {
                this?.let {
                    this.user?.apply {
                        sendEmailVerification().await().run {
                            signOutUser()
                            emit(Response.Success(true))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }
}







