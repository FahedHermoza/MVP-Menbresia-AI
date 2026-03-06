package com.fahed.perupass.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahed.perupass.designsystem.MenbresiaColors

private val ButtonShape = RoundedCornerShape(8.dp)
private val ActiveGradient = Brush.horizontalGradient(
    colors = listOf(MenbresiaColors.GoldLight, MenbresiaColors.Primary, MenbresiaColors.GoldDark)
)

@Composable
fun ActivateButton(
    isActive: Boolean,
    activeLabel: String,
    disabledLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.97f,
        animationSpec = tween(300),
        label = "button_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .scale(scale)
            .clip(ButtonShape)
            .then(
                if (isActive) {
                    Modifier.background(ActiveGradient)
                } else {
                    Modifier
                        .background(MenbresiaColors.SurfaceVariant)
                        .border(BorderStroke(1.dp, MenbresiaColors.BorderSubtle), ButtonShape)
                }
            )
            .clickable(enabled = isActive, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isActive) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Bolt,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = activeLabel,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        } else {
            Text(
                text = disabledLabel,
                color = MenbresiaColors.TextDisabled,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
