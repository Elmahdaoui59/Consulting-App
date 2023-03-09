package com.eldebvs.consulting.domain.model

data class Chatroom(
    val chatroom_name: String? = null,
    val chatroom_id: String? = null,
    val creator_id: String? = null,
    val security_level: String? = null,
    val chatroom_messages: List<ChatMessage> = emptyList()
)
