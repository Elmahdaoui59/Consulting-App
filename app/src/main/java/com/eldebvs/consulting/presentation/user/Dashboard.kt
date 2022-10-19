package com.eldebvs.consulting.presentation.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.eldebvs.consulting.presentation.auth.AuthenticationEvent
import com.eldebvs.consulting.presentation.auth.AuthenticationViewModel
import com.eldebvs.consulting.presentation.user.components.UserTopAppBar


@Composable
fun Dashboard(
    navController: NavController,
    authViewModel: AuthenticationViewModel
) {

    Scaffold(
        topBar = {
            UserTopAppBar(navController) {
                authViewModel.handleEvent(AuthenticationEvent.SignOutUser)
            }
       }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Employee Dashboard")
        }
    }
}