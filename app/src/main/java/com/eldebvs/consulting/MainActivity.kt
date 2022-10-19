package com.eldebvs.consulting

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.eldebvs.consulting.presentation.auth.AuthenticationViewModel
import com.eldebvs.consulting.presentation.common.UiEvent
import com.eldebvs.consulting.presentation.navigation.SetupNavGraph
import com.eldebvs.consulting.presentation.settings.SettingsEvent
import com.eldebvs.consulting.presentation.settings.SettingsViewModel
import com.eldebvs.consulting.presentation.settings.workers.CompressImageWorker
import com.eldebvs.consulting.ui.theme.ConsultingTheme
import com.eldebvs.consulting.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var authViewModel: AuthenticationViewModel



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

                    SetupNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                    )

                }

                LaunchedEffect(key1 = true) {
                    authViewModel.eventFlow.collectLatest { event ->
                        when (event) {
                            is UiEvent.ShowMessage -> {
                                Toast.makeText(
                                    this@MainActivity,
                                    getString(event.messLabel),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                            is UiEvent.Navigate -> {
                                val currentScreen = navController.currentDestination
                                navController.navigate(event.route) {
                                    currentScreen?.route?.let {
                                        popUpTo(it) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                            else -> {}
                        }
                    }

                }

            }
        }
    }
}
