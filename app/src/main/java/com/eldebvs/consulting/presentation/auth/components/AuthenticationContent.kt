package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.eldebvs.consulting.presentation.auth.AuthenticationEvent
import com.eldebvs.consulting.presentation.auth.AuthenticationState


@Composable
fun AuthenticationContent(
    modifier: Modifier = Modifier,
    authenticationState: AuthenticationState,
    handleEvent: (event: AuthenticationEvent) -> Unit
) {
    if (authenticationState.showResendEmailDialog) {
        ConfirmEmailDialog(
            onDismiss = { handleEvent(AuthenticationEvent.ToggleResendEmailDialogVisibility) },
            onEmailChanged = {
                handleEvent(AuthenticationEvent.EmailChanged(it))
            },
            onPasswordChanged = {
                handleEvent(AuthenticationEvent.PasswordChanged(it))
            },
            email = authenticationState.email,
            password = authenticationState.password
        ) {
            handleEvent(AuthenticationEvent.ResendVerificationEmail)
        }
    }
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        AuthenticationForm(
            authenticationMode = authenticationState.authenticationMode,
            email = authenticationState.email,
            password = authenticationState.password,
            onEmailChanged = {
                handleEvent(AuthenticationEvent.EmailChanged(it))
            },
            onPasswordChanged = {
                handleEvent(
                    AuthenticationEvent.PasswordChanged(
                        it
                    )
                )
            },
            onToggleAthMode = { handleEvent(AuthenticationEvent.ToggleAuthenticationMode) },
            passwordSatisfiedRequirement = authenticationState.passwordRequirements,
            onAuthenticate = { handleEvent(AuthenticationEvent.Authenticate) },
            enableAuthentication = authenticationState.isFormValid(),
            onShowResetEmailChanged = { handleEvent(AuthenticationEvent.ToggleResendEmailDialogVisibility) }
        )


    }


}
