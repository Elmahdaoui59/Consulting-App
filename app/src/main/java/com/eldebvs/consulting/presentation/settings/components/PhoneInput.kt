package com.eldebvs.consulting.presentation.settings.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.eldebvs.consulting.R

@Composable
fun PhoneInput(
    modifier: Modifier = Modifier,
    phone: String?,
    onPhoneChanged: (String) -> Unit
) {
    TextField(
        value = phone ?: "",
        onValueChange = {
            onPhoneChanged(it)
        },
        label = {
            Text(
                text = stringResource(id = R.string.label_phone)
            )
        },
        singleLine = true,
        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = "phone icon" ) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done )

    )
}