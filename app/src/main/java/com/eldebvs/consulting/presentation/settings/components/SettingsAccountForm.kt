package com.eldebvs.consulting.presentation.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.auth.components.EmailInput
import com.eldebvs.consulting.presentation.auth.components.PasswordInput
import com.eldebvs.consulting.presentation.auth.components.ResetPasswordButton
import com.eldebvs.consulting.presentation.settings.SettingsEvent
import com.eldebvs.consulting.presentation.settings.UserDetails

@Composable
fun SettingAccountForm(
    modifier: Modifier = Modifier,
    handleSettingsEvent: (event: SettingsEvent) -> Unit,
    userDetails: UserDetails
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.label_account_settings),
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
                        NameInput(
                            name = userDetails.name,
                            onNameChanged = { handleSettingsEvent(SettingsEvent.NameChanged(it)) },
                            modifier = modifier.fillMaxWidth()
                        )
                        Spacer(modifier = modifier.height(10.dp))
                        PhoneInput(
                            phone = userDetails.phone,
                            onPhoneChanged = { handleSettingsEvent(SettingsEvent.PhoneChanged(it)) },
                            modifier = modifier.fillMaxWidth()
                        )
                        Spacer(modifier = modifier.height(10.dp))
                        EmailInput(
                            email = userDetails.email,
                            onEmailChanged = { handleSettingsEvent(SettingsEvent.EmailChanged(it)) },
                            modifier = modifier.fillMaxWidth()
                        )
                        Spacer(modifier = modifier.height(10.dp))
                        PasswordInput(
                            modifier = Modifier.fillMaxWidth(),
                            password = userDetails.password,
                            onPasswordChanged = {
                                handleSettingsEvent(
                                    SettingsEvent.PasswordChanged(
                                        it
                                    )
                                )
                            }
                        )
                        Text(
                            text = stringResource(id = R.string.label_password_required_to_change_email),
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.overline,
                            color = Color.Yellow
                        )
                    }

                }
                Button(
                    onClick = { handleSettingsEvent(SettingsEvent.EditUserDetails) },
                    enabled = userDetails.isSettingFormValid()
                ) {
                    Text(text = stringResource(id = R.string.label_save_button_text))
                }
                ResetPasswordButton {
                    handleSettingsEvent(SettingsEvent.ResetUserPassword)
                }

            }
        }
    }
}