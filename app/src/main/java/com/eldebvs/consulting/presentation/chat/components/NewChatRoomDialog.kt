package com.eldebvs.consulting.presentation.chat.components

import android.widget.SeekBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun NewChatroomDialog() {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Column {
            Text(text = "New Chatroom Name:")
            Spacer(modifier = Modifier.width(10.dp))
            TextField(value = "chatroom name", onValueChange = {})
            Text(text = "Security level:")
            Spacer(modifier = Modifier.width(10.dp))
            SeekBar(LocalContext.current)
        }
    }
}