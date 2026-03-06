package com.fahed.perupass.feature.screen.validation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors

@Composable
fun PinKeyboard(
    onDigitPressed: (Int) -> Unit,
    onBackspacePressed: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val buttonHeight = 64.dp
    val gap = 12.dp

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {
        listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9)).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                row.forEach { digit ->
                    PinButton(
                        label = digit.toString(),
                        onClick = { onDigitPressed(digit) },
                        enabled = enabled,
                        modifier = Modifier
                            .weight(1f)
                            .height(buttonHeight)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(gap)
        ) {
            Box(modifier = Modifier.weight(1f).height(buttonHeight))

            PinButton(
                label = "0",
                onClick = { onDigitPressed(0) },
                enabled = enabled,
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(buttonHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (enabled) MenbresiaColors.SurfaceVariant else MenbresiaColors.IndicatorEmpty
                    )
                    .clickable(enabled = enabled) { onBackspacePressed() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = stringResource(R.string.pin_backspace_description),
                    tint = if (enabled) MenbresiaColors.TextPrimary else MenbresiaColors.TextDisabled
                )
            }
        }
    }
}

@Composable
private fun PinButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (enabled) MenbresiaColors.SurfaceVariant else MenbresiaColors.IndicatorEmpty
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (enabled) MenbresiaColors.TextPrimary else MenbresiaColors.TextDisabled,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
