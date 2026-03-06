package com.fahed.perupass.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun Modifier.glow(
    color: Color,
    alpha: Float = 0.4f,
    blurRadius: Dp = 20.dp,
) = this.drawBehind {
    val transparentColor = android.graphics.Color.argb(
        (alpha * 255).toInt(),
        color.red.times(255).toInt(),
        color.green.times(255).toInt(),
        color.blue.times(255).toInt()
    )
    drawIntoCanvas {
        val paint = Paint().apply {
            val frameworkPaint = asFrameworkPaint()
            frameworkPaint.color = transparentColor
            frameworkPaint.setShadowLayer(
                blurRadius.toPx(),
                0f,
                0f,
                transparentColor
            )
        }
        it.drawRoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = 8.dp.toPx(),
            radiusY = 8.dp.toPx(),
            paint = paint
        )
    }
}

@Composable
fun ActivateButton(
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isActive) Color.Transparent else Color(0xFF282828)
    val borderColor = if (isActive) Color.Transparent else Color(0xFF3A3A3A)
    val textColor = if (isActive) Color.Black else Color(0xFF666666)

    val backgroundModifier = if (isActive) {
        Modifier
            .glow(Color(0xFFD4AF37), alpha = 0.4f, blurRadius = 20.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF5D060),
                        Color(0xFFD4AF37),
                        Color(0xFFA07820)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
    } else {
        Modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(8.dp))
            .then(backgroundModifier)
            .clickable(enabled = isActive, onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isActive) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = if (isActive) "ACTIVATE PASS NOW" else "Get Closer to Activate (Geofence Active)",
                color = textColor,
                fontSize = if (isActive) 14.sp else 12.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
