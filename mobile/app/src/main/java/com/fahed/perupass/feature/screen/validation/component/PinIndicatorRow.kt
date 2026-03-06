package com.fahed.perupass.feature.screen.validation.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.fahed.perupass.designsystem.MenbresiaColors
import kotlinx.coroutines.delay

@Composable
fun PinIndicatorRow(
    filledCount: Int,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    var shakeOffset by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isError) {
        if (isError) {
            for (offset in listOf(10f, -10f, 8f, -8f, 4f, 0f)) {
                shakeOffset = offset
                delay(50)
            }
            shakeOffset = 0f
        }
    }

    Row(
        modifier = modifier.offset(x = shakeOffset.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(4) { index ->
            val isFilled = index < filledCount
            val scale by animateFloatAsState(
                targetValue = if (isFilled) 1.2f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "dot_scale_$index"
            )
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .scale(scale)
                    .background(
                        color = if (isFilled) MenbresiaColors.Primary else MenbresiaColors.IndicatorEmpty,
                        shape = CircleShape
                    )
            )
        }
    }
}
