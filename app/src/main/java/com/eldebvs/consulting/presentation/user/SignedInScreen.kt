package com.eldebvs.consulting.presentation.user

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eldebvs.consulting.presentation.auth.AuthenticationEvent
import com.eldebvs.consulting.presentation.auth.AuthenticationViewModel
import com.eldebvs.consulting.presentation.navigation.Screen
import com.eldebvs.consulting.presentation.user.components.UserTopAppBar
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SignedInScreen(
    authViewModel: AuthenticationViewModel
) {

    Scaffold(
        topBar = {
            UserTopAppBar {
                authViewModel.handleEvent(AuthenticationEvent.SignOutUser)
            }
       }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Signed In Screen")
        }
    }
}