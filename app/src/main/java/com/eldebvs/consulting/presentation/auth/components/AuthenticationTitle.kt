package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.eldebvs.consulting.R
import com.eldebvs.consulting.presentation.auth.AuthenticationMode


@Composable
fun AuthenticationTitle(
    modifier: Modifier,
    authenticationMode: AuthenticationMode
) {
    Text(
        text = stringResource(
            id = if (authenticationMode == AuthenticationMode.SIGN_IN)
                R.string.label_sign_In
            else R.string.label_sign_Up
        ),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )

}