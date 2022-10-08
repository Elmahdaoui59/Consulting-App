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
import androidx.lifecycle.LifecycleOwner
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
    private lateinit var settingsViewModel: SettingsViewModel


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
                    settingsViewModel = hiltViewModel()
                    SetupNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        settingsViewModel = settingsViewModel
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
            val userDetails = settingsViewModel.userDetails.collectAsState().value
            val workManager = WorkManager.getInstance(application)

            fun compressPhoto() {
                Log.d("worker", "compression start")
                val builder = Data.Builder()
                userDetails.profile_photo_uri?.let {
                    builder.putString(Constants.KEY_IMAGE_URI, it.toString())
                }
                val imageUri = builder.build()
                val compressRequest = OneTimeWorkRequestBuilder<CompressImageWorker>()
                    .setInputData(imageUri)
                    .build()
                workManager.beginUniqueWork(
                    "compress",
                    ExistingWorkPolicy.KEEP,
                    compressRequest
                ).enqueue()
                val workInfo = workManager.getWorkInfosForUniqueWorkLiveData("compress")

                val workObserver = Observer<MutableList<WorkInfo>> {
                    if (it[0].state.isFinished) {
                        val outputData = it[0].outputData.getString(Constants.WORKER_OUTPUT)
                        val uri = Uri.parse(outputData)
                        settingsViewModel.handleSettingEvent(
                            SettingsEvent.UploadPhotoToFirebase(
                                uri
                            )
                        )
                        Log.d("worker output", uri.toString())
                    }
                }
                workInfo.observe(this, workObserver)
            }
            if (userDetails.startCompression) {
                compressPhoto()
            }
        }
    }
}
