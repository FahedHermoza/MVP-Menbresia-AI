package com.fahed.perupass.feature.screen.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fahed.perupass.designsystem.MenbresiaColors

@Composable
fun GoldRadialGlow() {
    Box(
        modifier = Modifier
            .size(400.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MenbresiaColors.Primary.copy(alpha = 0.08f),
                        Color.Transparent
                    )
                )
            )
    )
}