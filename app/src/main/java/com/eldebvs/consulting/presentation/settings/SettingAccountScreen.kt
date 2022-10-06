package com.eldebvs.consulting.presentation.settings

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.auth.components.ErrorDialog
import com.eldebvs.consulting.presentation.common.UiEvent
import com.eldebvs.consulting.presentation.settings.components.SettingAccountForm
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
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            handleSettingsEvent(SettingsEvent.GetLocalProfilePhotoUri(it))
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


