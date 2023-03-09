package com.eldebvs.consulting.domain.repository

import com.eldebvs.consulting.domain.model.Chatroom
import com.eldebvs.consulting.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun createChatRoom(chatroomName: String, securityLevel: Int): Flow<Response<Boolean>>
    fun loadChatrooms(): Flow<Response<List<Chatroom>>>
}