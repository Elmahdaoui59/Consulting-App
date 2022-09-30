package com.eldebvs.consulting.presentation.auth


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.eldebvs.consulting.presentation.auth.components.AuthenticationContent


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


