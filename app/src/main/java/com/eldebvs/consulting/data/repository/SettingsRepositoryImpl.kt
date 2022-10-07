package com.eldebvs.consulting.data.repository

import android.util.Log
import com.eldebvs.consulting.util.Constants
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.model.User
import com.eldebvs.consulting.domain.repository.SettingsRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: DatabaseReference
) : SettingsRepository {


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

            val query1: Query = db.child(Constants.DATABASE_USER_NODE).orderByKey()
                .equalTo(auth.currentUser?.uid)
            val eventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot: DataSnapshot in snapshot.children) {
                        val data = singleSnapshot.getValue(User::class.java)
                        user = user.copy(name = data?.name, phone = data?.phone)
                        trySend(Response.Success(user))
                        Log.d("Database query ", "Found user $user")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Response.Failure(error.toException()))
                }
            }
            query1.addListenerForSingleValueEvent(eventListener)
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