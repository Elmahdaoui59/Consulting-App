package com.eldebvs.consulting.presentation.chat.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eldebvs.consulting.presentation.settings.components.ProfilePhoto

@Preview
@Composable
fun ChatMessageItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ProfilePhoto(
                url = null,
                handleSettingsEvent = {}
            )
            Text(text = "Jim", fontSize = 10.sp, color = Color.Gray)
        }
        Text(text = "Hello", modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp))
    }
}