package com.eldebvs.consulting.presentation.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun UploadPhotoDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onOptionChosen: (PhotoSource) -> Unit
) {


    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties()
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PhotoSourceOption(
                permName = "Take a photo with camera",
                icon = Icons.Default.AddAPhoto
            ) {
                onOptionChosen(PhotoSource.CAMERA)
            }
            PhotoSourceOption(
                permName = "Choose from Storage",
                icon = Icons.Default.AddPhotoAlternate
            ) {
                onOptionChosen(PhotoSource.STORAGE)
            }
        }
    }
}

@Composable
fun PhotoSourceOption(permName: String, icon: ImageVector, onOptionChosen: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(5.dp)
            .clickable { onOptionChosen() },
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "permission icon",
                modifier = Modifier.padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = permName, Modifier.padding(16.dp))
        }
    }
}