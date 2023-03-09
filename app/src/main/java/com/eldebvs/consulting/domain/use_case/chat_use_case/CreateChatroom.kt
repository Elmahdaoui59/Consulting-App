package com.eldebvs.consulting.domain.use_case.chat_use_case

import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.repository.ChatRepository
import kotlinx.coroutines.flow.flow

class CreateChatroom(
    private val repository: ChatRepository
) {
    operator fun invoke(chatroomName: String, securityLevel: Int) = run {
        if (chatroomName.isBlank()) {
            flow {
                emit(Response.Failure(Exception("Please enter a valid room name")))
            }
        } else {
            repository.createChatRoom(chatroomName, securityLevel)
        }
    }
}

