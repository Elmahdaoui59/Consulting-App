package com.eldebvs.consulting.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eldebvs.consulting.presentation.chat.components.ChatroomItem
import com.eldebvs.consulting.presentation.chat.components.CreateChatroomDialog
import com.eldebvs.consulting.presentation.common.ErrorDialog
import com.eldebvs.consulting.presentation.navigation.Screen

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel,
    navController: NavController
) {

    val uiState = chatViewModel.chatUiState.collectAsState().value
    if (uiState.showCreateChatRoomDialog) {
        CreateChatroomDialog(
            securityLevel = uiState.securityLevel,
            chatroomName = uiState.chatroomName,
            isLoading = uiState.isLoading,
            onChatroomNameChanged = {
                chatViewModel.handleChatEvent(ChatEvent.ChatroomNameChanged(it))
            },
            onSecurityLevelChanged = {
                chatViewModel.handleChatEvent(ChatEvent.SecurityLevelChanged(it))
            },
            onCreateChatroom = {
                chatViewModel.handleChatEvent(ChatEvent.CreateChatRoom)
            }
        ) { chatViewModel.handleChatEvent(ChatEvent.ToggleShowCreateChatRoomDialog) }

    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    chatViewModel.handleChatEvent(ChatEvent.ToggleShowCreateChatRoomDialog)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AddComment,
                    contentDescription = "add chatroom icon"
                )
            }
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            modifier = Modifier.padding(bottom = 70.dp)
        ) {
            items(20) {
                ChatroomItem(
                    onChatRoomClicked = { navController.navigate(Screen.ChatSession.route) }
                )
            }
        }

    }
    uiState.error?.let {
        ErrorDialog(error = it) {
            chatViewModel.handleChatEvent(ChatEvent.DismissError)
        }
    }

}

