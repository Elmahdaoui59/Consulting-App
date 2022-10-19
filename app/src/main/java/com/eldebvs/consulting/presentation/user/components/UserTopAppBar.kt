package com.eldebvs.consulting.presentation.user.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.navigation.Screen

@Composable
fun UserTopAppBar(
    navController: NavController,
    onLogOutAction: () -> Unit
) {
    var mDisplayMenu by remember {
        mutableStateOf(false)
    }
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {

            IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "menu icon")
            }
            DropdownMenu(expanded = mDisplayMenu, onDismissRequest = { mDisplayMenu = false }) {
                DropdownMenuItem(onClick = { navController.navigate(route = Screen.SettingAccountScreen.route) }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "account icon"
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Account Settings")
                }

                DropdownMenuItem(onClick = { onLogOutAction() }) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "logout icon")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Logout")
                }
                DropdownMenuItem(
                    onClick = { navController.navigate(route = Screen.ChatScreen.route) }
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = "chat icon")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Chat")
                }
            }
        },
    )
}
