package com.eldebvs.consulting.data.repository

import android.util.Log
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.model.User
import com.eldebvs.consulting.domain.repository.SettingsRepository
import com.eldebvs.consulting.util.Constants
import com.eldebvs.consulting.util.Constants.DATABASE_FIELD_PROFILE_IMAGE
import com.eldebvs.consulting.util.Constants.DATABASE_USER_NODE
import com.eldebvs.consulting.util.Constants.MB
import com.eldebvs.consulting.util.Constants.MB_THRESHOLD
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: DatabaseReference,
    private val storage: StorageReference
) : SettingsRepository {


    override suspend fun uploadImageToFirebase(mBytes: ByteArray): Response<Boolean> {
        try {
            val storageReference =
                storage.child("images/users/" + auth.currentUser?.uid + "/profile_image")
            if ((mBytes.size / MB) < MB_THRESHOLD) {
                val uploadTask: UploadTask = storageReference.putBytes(mBytes)
                uploadTask.await().run {
                    if (this.task.isSuccessful) {
                        //now save the download url into the firebase database
                        this.storage.downloadUrl.await().run {
                            if (this != null) {
                                Log.d("firebase url", this.toString())
                                auth.currentUser?.uid?.let {
                                    db.child(DATABASE_USER_NODE).child(it)
                                        .child(DATABASE_FIELD_PROFILE_IMAGE)
                                        .setValue(this.toString())
                                }
                            } else
                                throw Exception("download url null!")
                        }
                    } else
                        throw Exception(this.task.exception)
                }
            } else {
                throw Exception("File too large")
            }
        } catch (e: Exception) {
            return Response.Failure(e)
        }
        return Response.Success(true)
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

    override suspend fun getUserDetails(): Flow<Response<User?>> = callbackFlow {
        try {
            trySend(Response.Loading)
            var user = User()
            user = user.copy(email = auth.currentUser?.email)

            val query1: Query = db.child(DATABASE_USER_NODE).orderByKey()
                .equalTo(auth.currentUser?.uid)
            val eventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot: DataSnapshot in snapshot.children) {
                        val data = singleSnapshot.getValue(User::class.java)
                        user = user.copy(
                            name = data?.name,
                            phone = data?.phone,
                            profile_image = data?.profile_image
                        )
                        this@callbackFlow.trySendBlocking(Response.Success(user))
                        //trySend(Response.Success(user))
                        Log.d("Database query ", "Found user $user")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Response.Failure(error.toException()))
                }
            }
            query1.addValueEventListener(eventListener)
            awaitClose { query1.removeEventListener(eventListener) }
        } catch (e: Throwable) {
            trySend(Response.Failure(e))
        }


    }

    override suspend fun editUserDetails(name: String?, phone: String?): Flow<Response<Boolean>> =
        flow {
            try {
                auth.currentUser?.uid?.let { uid ->
                    emit(Response.Loading)
                    name?.let { name ->
                        db.child(Constants.DATABASE_USER_NODE).child(uid)
                            .child(Constants.DATABASE_FIELD_NAME)
                            .setValue(name).await().run {
                                emit(Response.Success(true))
                            }
                    }
                    phone?.let { phone ->
                        db.child(Constants.DATABASE_USER_NODE).child(uid)
                            .child(Constants.DATABASE_FIELD_PHONE)
                            .setValue(phone).await().run {
                                emit(Response.Success(true))
                            }
                    }
                }
            } catch (e: Throwable) {
                emit(Response.Failure(e))
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
}