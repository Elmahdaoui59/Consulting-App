package com.eldebvs.consulting.presentation.chat.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ChatSession() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Welcome to the new chatroom")
        LazyColumn(modifier = Modifier) {
            items(count = 3) {
                ChatMessageItem()
            }

        }
        Spacer(modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(text = "new message") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}