package com.fahed.perupass.feature.screen.placeholder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors

/**
 * Temporary placeholder for the Vibe Feed screen.
 * Will be replaced by SPEC-002 implementation.
 */
@Composable
fun FeedPlaceholderScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MenbresiaColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.feed_coming_soon),
            color = MenbresiaColors.Primary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
