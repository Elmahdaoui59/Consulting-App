package com.eldebvs.consulting.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CreateChatroomDialog(
    securityLevel: Int,
    chatroomName: String,
    isLoading: Boolean,
    onChatroomNameChanged: (String) -> Unit,
    onSecurityLevelChanged: (Int) -> Unit,
    onCreateChatroom: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            }
            Column(
                modifier = Modifier.background(color = MaterialTheme.colors.background).padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "New Chatroom Name:")
                Spacer(modifier = Modifier.height(5.dp))
                TextField(value = chatroomName, onValueChange = {
                    onChatroomNameChanged(it)
                })
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Security level:")
                Spacer(modifier = Modifier.height(5.dp))
                MySeekBar(level = securityLevel) {
                    onSecurityLevelChanged(it)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(50.dp))
                    Button(onClick = { onCreateChatroom() }) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}