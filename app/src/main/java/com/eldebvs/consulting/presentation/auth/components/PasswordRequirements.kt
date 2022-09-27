package com.eldebvs.consulting.presentation.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eldebvs.consulting.presentation.auth.PasswordRequirement


@Composable
fun PasswordRequirements(
    satisfiedRequirements: List<PasswordRequirement>
) {
    Column(
        modifier = Modifier
            .padding(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        RequirementField(PasswordRequirement.NUMBER,satisfiedRequirements)
        RequirementField(PasswordRequirement.CAPITAL_LETTER,satisfiedRequirements)
        RequirementField(PasswordRequirement.EIGHT_CHARACTERS,satisfiedRequirements)
    }
}

@Composable
fun RequirementField(
    passwordRequirement: PasswordRequirement,
    satisfiedRequirements: List<PasswordRequirement>
) {

    var color: Color = Color.LightGray
    if (passwordRequirement in satisfiedRequirements) {
        color = Color.Green
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Check, contentDescription = "check mark",
            modifier = Modifier.size(12.dp),
            tint = color
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(id = passwordRequirement.label),
            fontWeight = FontWeight.Light,
            color = color,
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )
    }
}