package com.eldebvs.consulting.data.repository

import android.net.Uri
import android.util.Log
import androidx.compose.ui.res.stringResource
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }

    override suspend fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { authe: FirebaseAuth ->
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

    override fun getUserDetails() {
        val user: FirebaseUser? = auth.currentUser
        if (user != null) {
            val uid: String = user.uid
            val name: String? = user.displayName
            val email: String? = user.email
            val photoUrl: Uri? = user.photoUrl
            val properties = "uid: " + uid + "\n" +
                    "name: " + name + "\n" +
                    "email: " + email + "\n" +
                    "photoUrl: " + photoUrl
            Log.i("User_Details \n", properties)
        } else {
            Log.i("User_Details", "The user is null")
        }
    }

    override suspend fun setUserDetails(name: String): Flow<Response<Boolean>> =
        flow {
            val user: FirebaseUser? = auth.currentUser
            if (user != null) {
                try {
                    val profileUpdates: UserProfileChangeRequest =
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .setPhotoUri(Uri.parse("https://www.sysbunny.com/blog/wp-content/uploads/2019/06/How-Android-App-Development-Boost-Your-Business-Growth-to-the-Next-Level.jpg"))
                            .build()
                    user.updateProfile(profileUpdates).await().run {
                        emit(Response.Success(true))
                    }
                } catch (e: Exception) {
                    emit(Response.Failure(e))
                }
            } else {
                Log.i("User_Details", "The user is null")
            }
        }

    override suspend fun resetUserPassword(): Flow<Response<Boolean>> =
        flow {
            try {
                emit(Response.Loading)
                auth.currentUser?.email?.let {
                    auth.sendPasswordResetEmail(it).await().run {
                        emit(Response.Success(true))
                    }
                }
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }

        }

    override suspend fun editUserEmail(email: String, password: String): Flow<Response<Boolean>> =
        flow {
            try {

                auth.currentUser?.let {
                    if (it.email.equals(email)) {

                        throw Throwable(message = "No change were made")

                    } else {
                        emit(Response.Loading)

                        val credential: AuthCredential? =
                            it.email?.let { it1 -> EmailAuthProvider.getCredential(it1, password) }

                        if (credential != null) {
                            it.reauthenticate(credential).await().run {
                                it.verifyBeforeUpdateEmail(email).await().run {
                                    emit(Response.Success(true))
                                }
                            }
                        }
                    }
                }

            } catch (e: Throwable) {
                emit(Response.Failure(e))
            }
        }

}







