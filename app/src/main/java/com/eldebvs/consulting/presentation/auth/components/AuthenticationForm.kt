package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eldebvs.consulting.presentation.auth.AuthenticationMode
import com.eldebvs.consulting.presentation.auth.PasswordRequirement


@Composable
fun AuthenticationForm(
    modifier: Modifier = Modifier,
    authenticationMode: AuthenticationMode,
    email: String?,
    password: String?,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onToggleAthMode: () -> Unit,
    passwordSatisfiedRequirement: List<PasswordRequirement>,
    onAuthenticate: () -> Unit,
    enableAuthentication: Boolean?,
    onShowResetEmailChanged: () -> Unit
) {

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = modifier.height(32.dp))
        AuthenticationTitle(modifier = modifier, authenticationMode = authenticationMode)

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
                    onEmailChanged = onEmailChanged,
                    modifier = modifier.fillMaxWidth()
                )
                Spacer(modifier = modifier.height(10.dp))
                PasswordInput(
                    password = password,
                    onPasswordChanged = onPasswordChanged,
                    modifier = modifier.fillMaxWidth()
                )
                Spacer(modifier = modifier.height(20.dp))
                AnimatedVisibility(visible = authenticationMode == AuthenticationMode.SIGN_UP) {
                    PasswordRequirements(satisfiedRequirements = passwordSatisfiedRequirement)
                }
                Spacer(modifier = modifier.height(10.dp))
                AuthenticationButton(
                    authenticationMode = authenticationMode,
                    enableAuthentication = enableAuthentication
                ) {
                    onAuthenticate()
                }
                Spacer(modifier = modifier.height(20.dp))

                ToggleAuthenticationMode(
                    authenticationMode = authenticationMode,
                    toggleAuthenticationAction = onToggleAthMode
                )
                ResendVerificationEmailButton {
                     onShowResetEmailChanged()
                }
            }

        }
    }

}
