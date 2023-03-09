package com.eldebvs.consulting.domain.use_case.chat_use_case

import com.eldebvs.consulting.domain.repository.ChatRepository

class LoadChatrooms(
    private val repository: ChatRepository
) {
    operator fun invoke() = repository.loadChatrooms()
}