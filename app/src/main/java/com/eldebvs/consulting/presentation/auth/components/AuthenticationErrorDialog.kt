package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.eldebvs.consulting.R

@Composable
fun AuthenticationErrorDialog(
    modifier: Modifier = Modifier,
    error: String,
    onDismissError: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {onDismissError() },
        confirmButton = {
                        TextButton(onClick = { onDismissError() }) {
                            Text(text = stringResource(id = R.string.error_button_text))
                        }
        },
        title = {
            Text(text = stringResource(R.string.error_title), fontSize = 18.sp)
        },
        text = { Text(text = error) }
    )

}