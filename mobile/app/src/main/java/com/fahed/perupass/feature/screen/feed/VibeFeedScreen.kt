package com.fahed.perupass.feature.screen.feed

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors
import com.fahed.perupass.feature.screen.feed.component.BottomNavBar
import com.fahed.perupass.feature.screen.feed.component.BottomNavTab
import com.fahed.perupass.feature.screen.feed.component.VenueCard

@Composable
fun VibeFeedScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    onNavigateToPasses: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: VibeFeedViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var currentTab by remember { mutableStateOf(BottomNavTab.FEED) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        viewModel.onEvent(Event.LocationPermissionResult(granted))
    }

    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.navigateToDetail.collect { venueId ->
            onNavigateToDetail(venueId)
        }
    }

    Scaffold(
        containerColor = MenbresiaColors.Background,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            BottomNavBar(
                currentTab = currentTab,
                onTabSelected = { tab ->
                    currentTab = tab
                    when (tab) {
                        BottomNavTab.EXPLORE -> onNavigateToExplore()
                        BottomNavTab.PASSES -> onNavigateToPasses()
                        BottomNavTab.PROFILE -> onNavigateToProfile()
                        BottomNavTab.FEED -> Unit
                    }
                }
            )
        }
    ) { innerPadding ->
        // Only apply bottom padding so the image fills edge-to-edge under the status bar
        VibeFeedContent(
            state = state,
            onVenueClicked = { venueId -> viewModel.onEvent(Event.VenueClicked(venueId)) },
            onPageChanged = { page -> viewModel.onEvent(Event.PageChanged(page)) },
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}

@Composable
private fun VibeFeedContent(
    state: State,
    onVenueClicked: (String) -> Unit,
    onPageChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().background(MenbresiaColors.Background),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(color = MenbresiaColors.Primary)
            }
            state.error != null -> {
                Text(
                    text = stringResource(R.string.feed_error_loading),
                    color = MenbresiaColors.Error,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            state.venues.isEmpty() -> {
                Text(
                    text = stringResource(R.string.feed_empty),
                    color = MenbresiaColors.TextSecondary,
                    fontSize = 16.sp
                )
            }
            else -> {
                val pagerState = rememberPagerState(pageCount = { state.venues.size })

                LaunchedEffect(pagerState.currentPage) {
                    onPageChanged(pagerState.currentPage)
                }

                VerticalPager(
                    state = pagerState,
                    pageSize = PageSize.Fill,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val venue = state.venues[page]
                    VenueCard(
                        venue = venue,
                        onVenueClicked = onVenueClicked
                    )
                }
            }
        }
    }
}
