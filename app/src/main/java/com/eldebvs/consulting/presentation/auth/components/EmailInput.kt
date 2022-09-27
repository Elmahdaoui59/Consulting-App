package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.eldebvs.consulting.R

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    email: String?,
    onEmailChanged: (String) -> Unit
) {
    TextField(
        value = email ?: "",
        onValueChange = {
            onEmailChanged(it)
        },
        label = {
            Text(
                text = stringResource(id = R.string.label_email)
            )
        },
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "email icon" ) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done )

    )

}