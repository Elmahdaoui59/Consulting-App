package com.eldebvs.consulting.presentation.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.eldebvs.consulting.presentation.auth.AuthenticationEvent
import com.eldebvs.consulting.presentation.auth.AuthenticationViewModel
import com.eldebvs.consulting.presentation.settings.components.SettingAccountForm

@Composable
fun SettingAccountScreen(authViewModel: AuthenticationViewModel) {
    val userDetails = authViewModel.userDetails.collectAsState().value
    SettingAccountForm(
        handleSettingsEvent = authViewModel::handleSettingEvent,
        userDetails = userDetails
    )
}


