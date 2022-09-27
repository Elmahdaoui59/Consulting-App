package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.eldebvs.consulting.R

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    password: String?,
    onPasswordChanged: (String) -> Unit
) {
    var isPasswordHidden by remember { mutableStateOf(true) }
    TextField(
        value = password ?: "",
        onValueChange = {
            onPasswordChanged(it)
        },
        visualTransformation =
            if (isPasswordHidden) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        ,
        label = {
            Text(
                text = stringResource(id = R.string.label_password)
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Password,
                contentDescription = "password icon"
            )
        },
        trailingIcon = {
            Icon(
                imageVector = if (isPasswordHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "password visibility",
                modifier = Modifier.clickable(
                    onClickLabel = if (isPasswordHidden) {
                        stringResource(id = R.string.show_password)
                    } else {
                        stringResource(
                            id = R.string.hide_password
                        )
                    }
                ) {
                    isPasswordHidden = !isPasswordHidden
                }
            )
        },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password )

    )

}