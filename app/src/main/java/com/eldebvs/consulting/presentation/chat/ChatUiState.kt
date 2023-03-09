package com.eldebvs.consulting.presentation.chat


data class ChatUiState(
    val showCreateChatRoomDialog: Boolean = false,
    val securityLevel: Int = 0,
    val chatroomName: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)
