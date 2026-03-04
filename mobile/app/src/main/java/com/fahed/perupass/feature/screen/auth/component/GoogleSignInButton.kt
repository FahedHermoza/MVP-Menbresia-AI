package com.fahed.perupass.feature.screen.auth.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors

@Composable
fun GoogleSignInButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (isLoading) 0.7f else 1f,
        animationSpec = tween(300),
        label = "button_alpha"
    )

    Button(
        onClick = onClick,
        enabled = !isLoading,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MenbresiaColors.Surface,
            disabledContainerColor = MenbresiaColors.SurfaceVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .alpha(alpha)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MenbresiaColors.GoldLight.copy(alpha = 0.6f),
                        MenbresiaColors.GoldDark.copy(alpha = 0.6f)
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MenbresiaColors.Primary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.login_google_icon_letter),
                    color = MenbresiaColors.Primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.login_google_button),
                    color = MenbresiaColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}