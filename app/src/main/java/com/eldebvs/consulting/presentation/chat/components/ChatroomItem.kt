package com.eldebvs.consulting.presentation.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eldebvs.consulting.presentation.settings.components.ProfilePhoto

@Composable
fun ChatroomItem(
    modifier: Modifier = Modifier,
    url: String? = null,
    creatorName: String? = null,
    messagesNumber: String? = null,
    onChatRoomClicked: () -> Unit,
    onDeleteChatroom: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onChatRoomClicked() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfilePhoto(
                url,
                handleSettingsEvent = {},
            )
            Text(text = "Created by", modifier = Modifier.padding(horizontal = 4.dp))
            Text(text = creatorName ?: "Jim")
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onDeleteChatroom() }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "new chat")
            Text(text = messagesNumber ?: "1 message")
        }
    }
}