package com.fahed.perupass.feature.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors
import com.fahed.perupass.designsystem.component.ActivateButton
import com.fahed.perupass.domain.model.Venue
import com.fahed.perupass.feature.screen.detail.component.BenefitCard
import com.fahed.perupass.feature.screen.detail.component.VenueImageHeader
import com.fahed.perupass.feature.screen.detail.model.MemberTier

@Composable
fun VenueDetailScreen(
    venueId: String,
    onNavigateBack: () -> Unit,
    onNavigateToPinValidation: (String) -> Unit,
    viewModel: VenueDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is DetailSideEffect.NavigateBack -> onNavigateBack()
                is DetailSideEffect.NavigateToPinValidation ->
                    onNavigateToPinValidation(effect.venueId)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MenbresiaColors.Background)
    ) {
        when (val current = state) {
            is DetailUiState.Loading -> {
                CircularProgressIndicator(
                    color = MenbresiaColors.Primary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is DetailUiState.Error -> {
                Text(
                    text = current.message,
                    color = MenbresiaColors.Error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }

            is DetailUiState.Success -> {
                VenueDetailContent(
                    state = current,
                    onClose = { viewModel.onIntent(DetailIntent.CloseClicked) },
                    onActivate = { viewModel.onIntent(DetailIntent.ActivateClicked) }
                )
            }
        }
    }
}

@Composable
private fun VenueDetailContent(
    state: DetailUiState.Success,
    onClose: () -> Unit,
    onActivate: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        VenueImageHeader(
            imageUrl = state.venue.imageUrl,
            venueName = state.venue.name,
            onClose = onClose,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
        )

        Column(
            modifier = Modifier
                .weight(0.6f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            VenueInfoRow(venue = state.venue)

            HorizontalDivider(color = MenbresiaColors.Border, thickness = 1.dp)

            MemberBenefitsSection(venue = state.venue, userTier = state.userTier)

            GeofenceStatusText(isInRange = state.isInRange, distanceText = state.distanceText)

            ActivateButton(
                modifier = Modifier.padding(bottom = 32.dp),
                isActive = state.isInRange,
                activeLabel = stringResource(R.string.detail_activate_button_active),
                disabledLabel = stringResource(R.string.detail_activate_button_disabled),
                onClick = onActivate
            )
        }
    }
}

@Composable
private fun VenueInfoRow(venue: Venue) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = venue.address,
                color = MenbresiaColors.TextSecondary,
                fontSize = 12.sp
            )
            Text(
                text = stringResource(
                    R.string.detail_hours_days,
                    venue.hours,
                    venue.days
                ),
                color = MenbresiaColors.TextSecondary,
                fontSize = 12.sp
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = String.format("%.1f", venue.rating),
                color = MenbresiaColors.Primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.detail_rating_label),
                color = MenbresiaColors.TextSecondary,
                fontSize = 10.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun MemberBenefitsSection(venue: Venue, userTier: MemberTier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(14.dp)
                .background(MenbresiaColors.Primary)
        )
        Text(
            text = stringResource(R.string.detail_member_benefits_title),
            color = MenbresiaColors.TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MemberTier.entries.forEach { tier ->
            BenefitCard(
                tierName = stringResource(tier.labelRes),
                benefitText = venue.benefits[tier.key] ?: stringResource(R.string.detail_benefit_default),
                icon = tier.icon,
                isHighlighted = userTier == tier
            )
        }
    }
}

@Composable
private fun GeofenceStatusText(isInRange: Boolean, distanceText: String?) {
    val text = if (isInRange) {
        stringResource(R.string.detail_geofence_in_range)
    } else {
        stringResource(R.string.detail_geofence_out_of_range)
    }
    val color = if (isInRange) MenbresiaColors.Primary else MenbresiaColors.TextDisabled

    Text(
        text = text,
        color = color,
        fontSize = 11.sp,
        letterSpacing = 0.5.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}
