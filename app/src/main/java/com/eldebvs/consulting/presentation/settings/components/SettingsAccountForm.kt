package com.eldebvs.consulting.presentation.settings.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.auth.components.EmailInput
import com.eldebvs.consulting.presentation.auth.components.PasswordInput
import com.eldebvs.consulting.presentation.auth.components.ResetPasswordButton
import com.eldebvs.consulting.presentation.settings.SettingsEvent
import com.eldebvs.consulting.presentation.settings.SettingsUiState
import com.eldebvs.consulting.presentation.settings.UserDetails

@Composable
fun SettingAccountForm(
    modifier: Modifier = Modifier,
    handleSettingsEvent: (event: SettingsEvent) -> Unit,
    userDetails: UserDetails,
    settingsUiState: SettingsUiState,
) {


    if (settingsUiState.showUploadPhotoDialog) {
        UploadPhotoDialog(
            onDismiss = { handleSettingsEvent(SettingsEvent.DismissUploadPhotoDialog) },
            onOptionChosen = {
                handleSettingsEvent(SettingsEvent.UploadOptionChosen(it))
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        }
    ) {


        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            if (settingsUiState.isLoading) {
                CircularProgressIndicator()
            }
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

                val profilePhotoUrl = remember(key1 = userDetails.profile_photo_firebase_url) {
                    userDetails.profile_photo_firebase_url
                }
                ProfilePhoto(
                    url = profilePhotoUrl,
                    handleSettingsEvent = handleSettingsEvent
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

@Composable
fun ProfilePhoto(
    url: String? = null,
    handleSettingsEvent: (event: SettingsEvent) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(data =url)
            .apply(block = fun ImageRequest.Builder.() {
                crossfade(durationMillis = 500)
                error(R.drawable.baseline_person_24)
                placeholder(R.drawable.baseline_person_24)
            }).build()
    )
    Image(
        painter = painter,
        contentDescription = "profile image",
        modifier = Modifier
            .clip(CircleShape)
            .size(50.dp)
            .background(color = Color.Gray)
            .clickable {
                handleSettingsEvent(SettingsEvent.ChangeProfilePhoto)
            }
    )
}