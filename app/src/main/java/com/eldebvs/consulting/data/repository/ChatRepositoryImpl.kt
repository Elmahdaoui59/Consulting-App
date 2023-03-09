package com.eldebvs.consulting.data.repository

import android.util.Log
import com.eldebvs.consulting.domain.model.ChatMessage
import com.eldebvs.consulting.domain.model.Chatroom
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.model.User
import com.eldebvs.consulting.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

class ChatRepositoryImpl(
    private val auth: FirebaseAuth,
    private val db: DatabaseReference
) : ChatRepository {
    private var mUserSecurityLevel: Int? = null

    override fun createChatRoom(
        chatroomName: String,
        securityLevel: Int
    ): Flow<Response<Boolean>> =
        flow {
            try {
                emit(Response.Loading)
                getUserSecurityLevel()
                mUserSecurityLevel?.let {
                    if (it > securityLevel) {

                        Log.i("cch", "msecuritylevel : $mUserSecurityLevel \n securutylevel : $securityLevel")
                        //get the new chatroom unique id
                        val chatroomId = db.child("chatrooms").push().key

                        //get the user id
                        val creatorId = auth.currentUser?.uid

                        //create a unique id for the message
                        val messageId: String? = db.child("chatrooms").push().key


                        //create the chatroom
                        if (chatroomId != null && creatorId != null && messageId != null) {
                            val chatroom = Chatroom(
                                chatroom_name = chatroomName,
                                chatroom_id = chatroomId,
                                creator_id = creatorId,
                                security_level = securityLevel.toString(),
                            )
                            db.child("chatrooms")
                                .child(chatroomId)
                                .setValue(chatroom)
                            //create the message
                            val chatMessage = ChatMessage(
                                "Welcome to the new chatroom!",
                                timestamp = getTimestamp()
                            )
                            db.child("chatrooms")
                                .child("chatroomId")
                                .child("chatroom_messages")
                                .child("messageId")
                                .setValue(chatMessage)

                        } else {
                            Log.e("chatroom_id", "The chatroom id is null")
                            throw Exception("Some thing went wrong, check internet connection and try again")
                        }
                        emit(Response.Success(true))
                    } else throw Exception("insufficient security level")
                } //?: throw Exception("error getting the user security level")
            } catch (t: Throwable) {
                emit(Response.Failure(t))
            }
        }

    override fun loadChatrooms(): Flow<Response<List<Chatroom>>> =
        callbackFlow {
            var eventListener: ValueEventListener? = null
            var query: Query? = null
            var chatroom = Chatroom()
            try {
                trySend(Response.Loading)
                query = db.child("chatrooms")
                eventListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (singleSnapshot: DataSnapshot in snapshot.children) {
                            val data = singleSnapshot.children.getValue(Chatroom::class.java)
                            chatroom = chatroom.copy(
                                chatroom_name = data?.chatroom_name,
                                chatroom_id = data?.chatroom_id,
                                creator_id = data?.creator_id,
                                security_level = data?.security_level,
                                chatroom_messages = data?.chatroom_messages ?: emptyList()
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            }
        }

    private fun getTimestamp(): String {
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date())
    }

    private fun getUserSecurityLevel() {
        val query: Query = db.child("users").orderByKey()
            .equalTo(auth.currentUser?.uid)
        query.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot: DataSnapshot in snapshot.children) {
                        mUserSecurityLevel =
                            singleSnapshot.getValue(User::class.java)?.security_level?.toInt()!!
                        Log.d(
                            "UserSecurityLevel",
                            "onDataChange: users security level:$mUserSecurityLevel"
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}