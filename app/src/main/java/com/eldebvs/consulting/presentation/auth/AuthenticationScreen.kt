package com.eldebvs.consulting.presentation.auth


import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.eldebvs.consulting.presentation.auth.components.AuthenticationContent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@RootNavGraph(start = true)
@Destination
@Composable
fun AuthenticationScreen(
    navigator: DestinationsNavigator,
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    MaterialTheme {
        LaunchedEffect(key1 = true) {
            viewModel.eventFlow.collectLatest {event ->
                if (event is AuthenticationViewModel.UiEvent.ShowMessage)
                    Toast.makeText(context, context.getString(event.messLabel), Toast.LENGTH_SHORT).show()
            }
        }
        AuthenticationContent(
            modifier = Modifier.fillMaxWidth(),
            authenticationState = viewModel.uiState.collectAsState().value,
            handleEvent = viewModel::handleEvent
        )
    }
}


