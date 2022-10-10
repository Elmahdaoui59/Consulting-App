package com.eldebvs.consulting.presentation.settings

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.work.*
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.auth.components.ErrorDialog
import com.eldebvs.consulting.presentation.common.UiEvent
import com.eldebvs.consulting.presentation.settings.components.SettingAccountForm
import com.eldebvs.consulting.presentation.settings.workers.CleanupWorker
import com.eldebvs.consulting.presentation.settings.workers.CompressImageWorker
import com.eldebvs.consulting.presentation.settings.workers.UploadPhotoWorker
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys
import com.eldebvs.consulting.presentation.settings.workers.WorkerKeys.KEY_IMAGE_URI
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SettingAccountScreen(
    settingsViewModel: SettingsViewModel,
) {
    val userDetails = settingsViewModel.userDetails.collectAsState().value
    val settingsUiState = settingsViewModel.settingsUiState.collectAsState().value
    val handleSettingsEvent = settingsViewModel::handleSettingEvent
    val ctx = LocalContext.current
    val workManager = WorkManager.getInstance(ctx.applicationContext)

    if (userDetails.enableCompression) {
        CompressAndUploadPhoto(
            userDetails = userDetails,
            workManager = workManager,
        )
        handleSettingsEvent(SettingsEvent.DisableCompression)
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            Log.d("uri", it.toString())
            it?.let {
                //save the photo uri
                handleSettingsEvent(SettingsEvent.GetLocalProfilePhotoUri(it))
                handleSettingsEvent(SettingsEvent.EnableCompression)
            }
        }
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    ) { isGranted ->
        if (isGranted) {
        } else {

        }
    }

    val readStoragePermissionState = rememberPermissionState(
        permission = Manifest.permission.READ_EXTERNAL_STORAGE
    ) { isGranted ->
        if (isGranted) {
            launcher.launch("image/*")
        } else {
            Toast.makeText(
                ctx, ctx.getString(R.string.label_read_storage_permission_required),
                Toast.LENGTH_LONG
            ).show()

        }
    }

    SettingAccountForm(
        handleSettingsEvent = handleSettingsEvent,
        userDetails = userDetails,
        settingsUiState = settingsUiState
    )
    settingsUiState.error?.let {
        ErrorDialog(error = it) {
            settingsViewModel.handleSettingEvent(SettingsEvent.ErrorDismissed)
        }
    }
    LaunchedEffect(key1 = true) {
        settingsViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    Toast.makeText(
                        ctx,
                        ctx.getString(event.messLabel),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                is UiEvent.CheckReadStoragePermission -> {
                    readStoragePermissionState.launchPermissionRequest()
                }
                is UiEvent.CheckCameraPermission -> {
                    cameraPermissionState.launchPermissionRequest()
                }
                else -> {}
            }
        }
    }


}


@Composable
fun CompressAndUploadPhoto(
    userDetails: UserDetails,
    workManager: WorkManager,
) {
    //get the profile photo from local storage
    val builder = Data.Builder()
    val uri = remember(key1 = userDetails) {
        userDetails.profile_photo_uri
    }
    val imageUri = builder.putString(KEY_IMAGE_URI, uri.toString()).build()
    //construct the compress request
    val compressRequest = OneTimeWorkRequestBuilder<CompressImageWorker>()
        .setInputData(imageUri).build()

    //construct the upload work request
    val uploadRequest = OneTimeWorkRequestBuilder<UploadPhotoWorker>()
        .setConstraints(
            Constraints.Builder().setRequiredNetworkType(
                NetworkType.CONNECTED
            ).build()
        ).build()
    val cleanUpRequest = OneTimeWorkRequestBuilder<CleanupWorker>().build()
    val workInfo = workManager
        .getWorkInfosForUniqueWorkLiveData(WorkerKeys.COMPRESS_UNIQUE_WORK_NAME)
        .observeAsState().value

    workManager.beginUniqueWork(
        WorkerKeys.COMPRESS_UNIQUE_WORK_NAME,
        ExistingWorkPolicy.KEEP,
        compressRequest
    ).then(uploadRequest).then(cleanUpRequest)
        .enqueue()


}



