package com.eldebvs.consulting.presentation.chat.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun MySeekBar(
    levelsNumber: Int = 5,
    level: Int,
    onLevelChanged: (Int) -> Unit
) {

    var offsetX by remember {
        mutableStateOf(0f)
    }
    var dragPosition by remember {
        mutableStateOf(0f)
    }
    var width by remember {
        mutableStateOf(0f)
    }
    var step = 0f
    var updatedLevel by remember {
        mutableStateOf(level)
    }

    LaunchedEffect(key1 = offsetX) {
        Log.i("com", "comp in launchedEffect")
        for (i in 0 until levelsNumber) {
            dragPosition = if (offsetX > step * (i + 1 / 2)) {
                updatedLevel = i + 1
                step * (i + 1)
            } else {
                updatedLevel = i
                (step * i)
                break
            }
        }
        if (offsetX < width / levelsNumber / 2) {
            dragPosition = 0f
        }
    }

    Box(
        modifier = Modifier.padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            width = size.width
            step = width / levelsNumber
            dragPosition = level * step
            drawLine(
                color = Color.Gray,
                strokeWidth = 4.dp.toPx(),
                start = Offset(x = 0f, size.height / 2),
                end = Offset(x = size.width, size.height / 2)
            )
            drawLine(
                color = Color.Blue,
                strokeWidth = 4.dp.toPx(),
                start = Offset(x = 0f, size.height / 2),
                end = Offset(x = dragPosition, size.height / 2)
            )
            drawPoints(
                listOf(Offset(x = dragPosition, size.height / 2)),
                PointMode.Points,
                color = Color.Red,
                strokeWidth = 30f,
                cap = StrokeCap.Round
            )
        }
        Text(
            modifier = Modifier
                .offset { IntOffset(dragPosition.toInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX += delta
                        onLevelChanged(updatedLevel)
                    }
                )
                .padding(16.dp),
            text = "  "
        )
        Text(text = level.toString())
    }
}

























