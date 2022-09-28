package com.eldebvs.consulting.presentation.user.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable

@Composable
fun UserTopAppBar(
    onLogOutAction: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "Consulting") },
        actions = {
            IconButton(onClick = { onLogOutAction() }) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = "logout icon")
            }
        }
    )

}