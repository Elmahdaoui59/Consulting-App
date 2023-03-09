package com.eldebvs.consulting.presentation.chat

sealed class ChatEvent {
    class SecurityLevelChanged(val securityLevel:Int): ChatEvent()
    object ToggleShowCreateChatRoomDialog: ChatEvent()
    class ChatroomNameChanged(val chatroomName: String): ChatEvent()
    object DismissError: ChatEvent()
    object CreateChatRoom: ChatEvent()
}
