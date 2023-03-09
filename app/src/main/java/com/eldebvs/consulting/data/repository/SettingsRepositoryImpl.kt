package com.eldebvs.consulting.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.*
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.model.User
import com.eldebvs.consulting.domain.repository.SettingsRepository
import com.eldebvs.consulting.presentation.settings.workers.CleanupWorker
import com.eldebvs.consulting.presentation.settings.workers.CompressImageWorker
import com.eldebvs.consulting.presentation.settings.workers.UploadImageWorker
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.KEY_IMAGE_URI
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: DatabaseReference,
    private val storage: StorageReference,
    context: Context
) : SettingsRepository {

    private val workManager = WorkManager.getInstance(context)
    override fun compressAndUploadImage(imageUri: Uri) {

        val inputImage = Data.Builder().putString(KEY_IMAGE_URI, imageUri.toString()).build()

        //construct the upload work request
        val compressRequest = OneTimeWorkRequestBuilder<CompressImageWorker>().setInputData(inputImage).build()

        //construct the upload work request
        val uploadRequest = OneTimeWorkRequestBuilder<UploadImageWorker>()
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(
                    NetworkType.CONNECTED
                ).build()
            ).build()

        //construct the clean request
        val cleanUpRequest = OneTimeWorkRequestBuilder<CleanupWorker>().build()
        workManager.beginUniqueWork(
            WorkerKeys.COMPRESS_UNIQUE_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            compressRequest
        ).then(uploadRequest).then(cleanUpRequest).enqueue()
    }

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

    override fun editUserEmail(email: String, password: String): Flow<Response<Boolean>> =
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

    override fun getUserDetails(): Flow<Response<User?>> = callbackFlow {

        var eventListener: ValueEventListener? = null
        var query: Query? = null
        try {
            trySend(Response.Loading)
            var user = User()
            user = user.copy(email = auth.currentUser?.email)

            query = db.child(DATABASE_USER_NODE).orderByKey().equalTo(auth.currentUser?.uid)
            eventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot: DataSnapshot in snapshot.children) {
                        val data = singleSnapshot.getValue(User::class.java)
                        user = user.copy(
                            name = data?.name,
                            phone = data?.phone,
                            profile_image = data?.profile_image
                        )
                        //this@callbackFlow.trySendBlocking(Response.Success(user))
                        trySend(Response.Success(user))
                        Log.d("Database query ", "Found user $user")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Database error ", error.message)
                    //trySend(Response.Failure(Exception(LISTENER_CANCELED)))
                    trySend(Response.Failure(CancellationException()))
                }
            }
            query.addValueEventListener(eventListener)
        } catch (e: Throwable) {
            trySend(Response.Failure(e))
        }
        awaitClose {
            eventListener?.let {
                query?.removeEventListener(eventListener)
            }
        }
    }

    override fun editUserDetails(name: String?, phone: String?): Flow<Response<Boolean>> =
        flow {
            try {
                auth.currentUser?.uid?.let { uid ->
                    emit(Response.Loading)
                    name?.let { name ->
                        db.child(DATABASE_USER_NODE).child(uid)
                            .child(Constants.DATABASE_FIELD_NAME)
                            .setValue(name).await().run {
                                emit(Response.Success(true))
                            }
                    }
                    phone?.let { phone ->
                        db.child(DATABASE_USER_NODE).child(uid)
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

    override fun resetUserPassword(): Flow<Response<Boolean>> =
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