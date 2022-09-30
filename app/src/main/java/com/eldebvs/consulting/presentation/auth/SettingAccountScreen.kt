package com.eldebvs.consulting.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.auth.components.EmailInput
import com.eldebvs.consulting.presentation.auth.components.PasswordInput
import com.eldebvs.consulting.presentation.auth.components.ResetPasswordButton

@Composable
fun SettingAccountScreen(authViewModel: AuthenticationViewModel) {
    val uiState = authViewModel.uiState.collectAsState().value
    SettingAccountForm(
        onChangeEmailAction = { authViewModel.handleEvent(AuthenticationEvent.EditUserEmail) },
        email = uiState.email,
        password = uiState.password,
        enableChangeEmailAction = authViewModel.uiState.value.isFormValid(),
        handleEvent = authViewModel::handleEvent
    )
}

@Composable
fun SettingAccountForm(
    modifier: Modifier = Modifier,
    onChangeEmailAction: () -> Unit,
    enableChangeEmailAction: Boolean?,
    email: String?,
    password: String?,
    handleEvent: (event: AuthenticationEvent) -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.label_account_settings)) })
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.label_reset_your_email),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(23.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        EmailInput(
                            email = email,
                            onEmailChanged = { handleEvent(AuthenticationEvent.EmailChanged(it)) },
                            modifier = modifier.fillMaxWidth()
                        )
                        Spacer(modifier = modifier.height(10.dp))
                        PasswordInput(
                            modifier = Modifier.fillMaxWidth(),
                            password = password,
                            onPasswordChanged = { handleEvent(AuthenticationEvent.PasswordChanged(it)) }
                        )
                    }
                }
                Button(
                    onClick = { onChangeEmailAction() },
                    enabled = enableChangeEmailAction ?: false
                ) {
                    Text(text = stringResource(id = R.string.label_save_button_text))
                }
                ResetPasswordButton {
                    handleEvent(AuthenticationEvent.ResetUserPassword)
                }

            }
        }
    }
}
