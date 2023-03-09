package com.eldebvs.consulting.presentation.auth


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.eldebvs.consulting.presentation.auth.components.AuthenticationContent
import com.eldebvs.consulting.presentation.common.ErrorDialog


@Composable
fun AuthenticationScreen(
    authViewModel: AuthenticationViewModel
) {

    val uiState = authViewModel.uiState.collectAsState().value

    MaterialTheme {
        AuthenticationContent(
            modifier = Modifier.fillMaxWidth(),
            authenticationState = uiState,
            handleEvent = authViewModel::handleEvent
        )

        uiState.error?.let {
            ErrorDialog(error = it) {
                authViewModel.handleEvent(AuthenticationEvent.ErrorDismissed)
            }
        }
    }
}


