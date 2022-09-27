package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.auth.AuthenticationMode

@Composable
fun AuthenticationButton(
    authenticationMode: AuthenticationMode,
    enableAuthentication: Boolean?,
    authenticate: () -> Unit
) {
    Button(onClick = { authenticate() }, enabled = enableAuthentication ?: false) {
        Text(
            text = if (authenticationMode == AuthenticationMode.SIGN_UP) {
                stringResource(id = R.string.label_sign_Up)
            } else {
                stringResource(id = R.string.label_sign_In)
            }
        )
    }
}