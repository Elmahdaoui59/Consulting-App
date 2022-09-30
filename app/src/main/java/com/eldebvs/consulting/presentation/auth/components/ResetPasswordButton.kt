package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eldebvs.consulting.R

@Composable
fun ResetPasswordButton(
    modifier: Modifier = Modifier,
    onResetPasswordAction:  () -> Unit
) {
    Surface(modifier = modifier.padding(16.dp), elevation = 8.dp) {
        TextButton(
            modifier= Modifier.background(MaterialTheme.colors.surface)
                .fillMaxWidth()
                .padding(8.dp),
            onClick = { onResetPasswordAction() }
        ) {
            Text(
                text = stringResource(
                    id = R.string.reset_password
                ),
                fontSize = 13.sp
            )
        }
    }
}