package com.fahed.perupass.feature.screen.validation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors
import com.fahed.perupass.designsystem.component.CountdownTimer

private const val SUCCESS_TOTAL_SECONDS = 300

@Composable
fun PassActiveScreen(
    benefit: String,
    remainingSeconds: Int,
    modifier: Modifier = Modifier
) {
    var animationTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationTriggered = true
    }

    val checkScale by animateFloatAsState(
        targetValue = if (animationTriggered) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "check_scale"
    )
    val checkAlpha by animateFloatAsState(
        targetValue = if (animationTriggered) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "check_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MenbresiaColors.Background.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(checkScale)
                    .alpha(checkAlpha)
                    .border(
                        width = 3.dp,
                        color = MenbresiaColors.Success,
                        shape = CircleShape
                    )
                    .background(
                        color = MenbresiaColors.Success.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.pass_check_description),
                    tint = MenbresiaColors.Success,
                    modifier = Modifier.size(52.dp)
                )
            }

            Text(
                text = stringResource(R.string.pass_title),
                color = MenbresiaColors.TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.pass_benefit_confirmed),
                color = MenbresiaColors.TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = benefit,
                color = MenbresiaColors.Success,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            CountdownTimer(
                remainingSeconds = remainingSeconds,
                totalSeconds = SUCCESS_TOTAL_SECONDS,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
