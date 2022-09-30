package com.eldebvs.consulting

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.eldebvs.consulting.presentation.auth.AuthenticationEvent
import com.eldebvs.consulting.presentation.auth.AuthenticationViewModel
import com.eldebvs.consulting.presentation.auth.components.AuthenticationErrorDialog
import com.eldebvs.consulting.presentation.navigation.SetupNavGraph
import com.eldebvs.consulting.ui.theme.ConsultingTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    lateinit var authViewModel: AuthenticationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            ConsultingTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    navController = rememberNavController()
                    authViewModel = hiltViewModel()
                    val uiState = authViewModel.uiState.collectAsState().value

                    SetupNavGraph(
                        navController = navController,
                        authViewModel = authViewModel
                    )
                    LaunchedEffect(key1 = true) {
                        authViewModel.eventFlow.collectLatest { event ->
                            when(event) {
                                is AuthenticationViewModel.UiEvent.ShowMessage -> {
                                    Toast.makeText(this@MainActivity, getString(event.messLabel), Toast.LENGTH_SHORT)
                                        .show()
                                }
                                is AuthenticationViewModel.UiEvent.Navigate -> {
                                    val currentScreen = navController.currentDestination
                                    navController.navigate(event.route) {
                                        currentScreen?.route?.let {
                                            popUpTo(it) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    uiState.error?.let {
                        AuthenticationErrorDialog(error = it) {
                            authViewModel.handleEvent(AuthenticationEvent.ErrorDismissed)
                        }
                    }
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }


}
