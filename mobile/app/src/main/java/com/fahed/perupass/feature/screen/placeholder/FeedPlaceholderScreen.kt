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

@Composable
fun ExploreScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(MenbresiaColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.placeholder_coming_soon),
            color = MenbresiaColors.TextSecondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PassesScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(MenbresiaColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.placeholder_coming_soon),
            color = MenbresiaColors.TextSecondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(MenbresiaColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.placeholder_coming_soon),
            color = MenbresiaColors.TextSecondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun VenueDetailPlaceholderScreen(venueId: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(MenbresiaColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.placeholder_venue_detail, venueId),
            color = MenbresiaColors.TextSecondary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
