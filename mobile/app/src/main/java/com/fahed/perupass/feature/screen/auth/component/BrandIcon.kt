package com.fahed.perupass.feature.screen.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors

@Composable
fun BrandIcon() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(96.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MenbresiaColors.Primary.copy(alpha = 0.25f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(50)
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(MenbresiaColors.GoldLight, MenbresiaColors.GoldDark)
                ),
                shape = RoundedCornerShape(50)
            )
    ) {
        Text(
            text = stringResource(R.string.login_brand_icon_letter),
            color = MenbresiaColors.Primary,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold
        )
    }
}