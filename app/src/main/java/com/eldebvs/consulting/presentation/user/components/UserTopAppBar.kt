package com.eldebvs.consulting.presentation.user.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.navigation.Screen

@Composable
fun UserTopAppBar(
    navController: NavController,
    onLogOutAction: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(onClick = { navController.navigate(route = Screen.SettingAccountScreen.route) }) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription ="account icon" )
            }
            IconButton(onClick = { onLogOutAction() }) {
                Icon(imageVector = Icons.Default.Logout, contentDescription = "logout icon")
            }
        },
    )

}
