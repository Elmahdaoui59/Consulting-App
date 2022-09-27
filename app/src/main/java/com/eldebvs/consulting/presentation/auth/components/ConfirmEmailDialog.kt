package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eldebvs.consulting.R


@Composable
fun ConfirmEmailDialog(
    onDismiss: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    email: String?,
    password: String?,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.confirm_dialog_title), fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize().padding(vertical = 10.dp),
                color = Color.Red
            )
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(id = R.string.cancel_dialog_button_text))
            }
            TextButton(onClick = { onConfirm() }) {
                Text(text = stringResource(id = R.string.confirm_dialog_button_text))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                EmailInput(email = email, onEmailChanged = {onEmailChanged(it)})
                Spacer(modifier = Modifier.height(10.dp))
                PasswordInput(password = password, onPasswordChanged = { onPasswordChanged(it)})
            }
        }
    )
}
