package com.eldebvs.consulting.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eldebvs.consulting.domain.model.Response
import com.eldebvs.consulting.domain.use_case.chat_use_case.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCases: ChatUseCases
) : ViewModel() {
    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()

    private fun loadChatrooms() {
        viewModelScope.launch {
            chatUseCases.loadChatrooms().collect { response ->

            }
        }
    }

    fun handleChatEvent(chatEvent: ChatEvent) {
        when (chatEvent) {
            is ChatEvent.ToggleShowCreateChatRoomDialog -> {
                toggleShowCreateChatroomDialog()
            }
            is ChatEvent.SecurityLevelChanged -> {
                _chatUiState.update {
                    it.copy(
                        securityLevel = chatEvent.securityLevel
                    )
                }
            }
            is ChatEvent.ChatroomNameChanged -> {
                _chatUiState.update {
                    it.copy(
                        chatroomName = chatEvent.chatroomName
                    )
                }
            }
            is ChatEvent.DismissError -> {
                resetChatUiState()
            }
            is ChatEvent.CreateChatRoom -> {
                createChatroom()
            }
        }
    }

    private fun createChatroom() {
        viewModelScope.launch {
            chatUseCases.createChatroom.invoke(
                chatroomName = chatUiState.value.chatroomName,
                securityLevel = chatUiState.value.securityLevel
            ).collect { response ->
                when (response) {
                    is Response.Failure -> {
                        updateError(response.e.message)
                    }
                    is Response.Loading -> {
                        updateLoadingState(true)
                    }
                    is Response.Success -> {
                        updateLoadingState(false)
                        toggleShowCreateChatroomDialog()
                    }
                }
            }
        }
    }

    private fun toggleShowCreateChatroomDialog() {
        _chatUiState.update {
            it.copy(
                showCreateChatRoomDialog = !it.showCreateChatRoomDialog,
                chatroomName = "",
                securityLevel = 0
            )
        }

    }

    private fun updateError(message: String?) {
        _chatUiState.update {
            it.copy(
                error = message
            )
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        _chatUiState.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }

    private fun resetChatUiState() {
        _chatUiState.value = ChatUiState()
    }
}