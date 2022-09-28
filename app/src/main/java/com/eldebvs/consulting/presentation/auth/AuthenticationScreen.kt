package com.eldebvs.consulting.presentation.auth


import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.eldebvs.consulting.presentation.auth.components.AuthenticationContent
import com.eldebvs.consulting.presentation.navigation.Screen
import kotlinx.coroutines.flow.collectLatest


@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel
) {

    val uiState = viewModel.uiState.collectAsState().value

    MaterialTheme {

        AuthenticationContent(
            modifier = Modifier.fillMaxWidth(),
            authenticationState = uiState,
            handleEvent = viewModel::handleEvent
        )
    }
}


