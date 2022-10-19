package com.eldebvs.consulting.presentation.chat

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.runtime.Composable

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {  }) {
                Icon(imageVector = Icons.Default.AddComment, contentDescription = "add chatroom icon")
            }
        }
    ) {}
}