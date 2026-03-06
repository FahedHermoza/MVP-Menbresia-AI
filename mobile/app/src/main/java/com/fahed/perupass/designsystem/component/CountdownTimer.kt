package com.fahed.perupass.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors

@Composable
fun CountdownTimer(
    remainingSeconds: Int,
    totalSeconds: Int,
    modifier: Modifier = Modifier
) {
    val progress = remainingSeconds.toFloat() / totalSeconds.toFloat()
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.pass_valid_for),
            color = MenbresiaColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.weight(1f),
            color = MenbresiaColors.Success,
            trackColor = MenbresiaColors.SurfaceVariant
        )
        Text(
            text = stringResource(R.string.pass_remaining_format, minutes, seconds),
            color = MenbresiaColors.Success,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
