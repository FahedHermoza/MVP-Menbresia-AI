package com.fahed.perupass.feature.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fahed.perupass.designsystem.component.ActivateButton
import com.fahed.perupass.feature.screen.detail.component.BenefitCard
import com.fahed.perupass.feature.screen.detail.component.VenueImageHeader
import kotlinx.coroutines.flow.collectLatest

@Composable
fun VenueDetailScreen(
    venueId: String,
    onNavigateBack: () -> Unit,
    onNavigateToPinValidation: (String) -> Unit,
    viewModel: VenueDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(venueId) {
        viewModel.onEvent(DetailEvent.LoadVenue(venueId))

        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is DetailSideEffect.NavigateBack -> onNavigateBack()
                is DetailSideEffect.NavigateToPinValidation -> onNavigateToPinValidation(effect.venueId)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111111))
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                color = Color(0xFFD4AF37),
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (state.error != null) {
            Text(
                text = state.error ?: "An error occurred",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            state.venue?.let { venue ->
                Column(modifier = Modifier.fillMaxSize()) {
                    VenueImageHeader(
                        imageUrl = venue.imageUrl,
                        venueName = venue.name,
                        onCloseClick = { viewModel.onEvent(DetailEvent.OnCloseClicked) },
                        modifier = Modifier.weight(0.4f) // Top ~40%
                    )

                    Column(
                        modifier = Modifier
                            .weight(0.6f) // Bottom ~60%
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Info Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = venue.address,
                                    color = Color(0xFFA0A0A0),
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${venue.days} • ${venue.hours}",
                                    color = Color(0xFFA0A0A0),
                                    fontSize = 12.sp
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFD4AF37),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = venue.rating.toString(),
                                    color = Color(0xFFD4AF37),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(color = Color(0xFF2A2A2A), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(20.dp))

                        // Member Benefits Title
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(width = 3.dp, height = 14.dp)
                                    .background(Color(0xFFD4AF37))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "MEMBER BENEFITS",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Benefit Cards
                        val tiers = listOf(
                            Pair("Vibe Member", venue.benefits["Vibe Member"] ?: "Free Entry before midnight"),
                            Pair("Gold Select", venue.benefits["Gold Select"] ?: "Fast-Pass + Free Drink"),
                            Pair("Black Founder", venue.benefits["Black Founder"] ?: "VIP Area + Concierge")
                        )

                        tiers.forEach { tier ->
                            BenefitCard(
                                tierName = tier.first,
                                benefit = tier.second,
                                isUserTier = tier.first == "Gold Select" // highlight Gold Select per mockup AC-2
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Geofence text and button at the bottom
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 24.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (state.isInRange) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFFD4AF37),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "✓ YOU ARE NEARBY — READY TO ACTIVATE",
                                        color = Color(0xFFD4AF37),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Radar,
                                        contentDescription = null,
                                        tint = Color(0xFF666666),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "GEOFENCE ACTIVE — YOU MUST BE NEARBY TO ACTIVATE",
                                        color = Color(0xFF666666),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))

                            ActivateButton(
                                isActive = state.isInRange,
                                onClick = { viewModel.onEvent(DetailEvent.OnActivateClicked(venue.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}
