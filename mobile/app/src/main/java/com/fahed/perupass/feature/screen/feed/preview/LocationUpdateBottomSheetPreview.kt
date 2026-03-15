package com.fahed.perupass.feature.screen.feed.preview

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fahed.perupass.feature.screen.feed.component.LocationUpdateBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun LocationUpdateBottomSheetPreview() {
    LocationUpdateBottomSheet(
        isRefreshing = false,
        errorMessage = null,
        onDismiss = {},
        onRefreshRequested = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun LocationUpdateBottomSheetLoadingPreview() {
    LocationUpdateBottomSheet(
        isRefreshing = true,
        errorMessage = null,
        onDismiss = {},
        onRefreshRequested = {}
    )
}