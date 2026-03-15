package com.fahed.perupass.feature.screen.validation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors
import com.fahed.perupass.domain.model.Venue
import com.fahed.perupass.feature.screen.validation.component.BenefitBadge
import com.fahed.perupass.feature.screen.validation.component.BlockedOverlay
import com.fahed.perupass.feature.screen.validation.component.HandleBar
import com.fahed.perupass.feature.screen.validation.component.PinIndicatorRow
import com.fahed.perupass.feature.screen.validation.component.PinKeyboard
import com.fahed.perupass.feature.screen.validation.model.ProfileHelper.CURRENT_BENEFIT

@Composable
fun PinValidationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToFeed: () -> Unit,
    viewModel: PinValidationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val venue by viewModel.venue.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                PinValidationSideEffect.NavigateBack -> onNavigateBack()
                PinValidationSideEffect.NavigateToFeed -> onNavigateToFeed()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MenbresiaColors.Background)
    ) {
        when (val current = state) {
            is PinValidationUiState.Success -> {
                PassActiveScreen(
                    benefit = current.benefit,
                    remainingSeconds = current.remainingSeconds
                )
            }

            else -> {
                PinPadContent(
                    state = state,
                    venue = venue,
                    onIntent = viewModel::onIntent
                )
            }
        }
    }
}

@Composable
private fun PinPadContent(
    state: PinValidationUiState,
    venue: Venue?,
    onIntent: (PinValidationIntent) -> Unit
) {
    val isBlocked = state is PinValidationUiState.Blocked
    val isValidating = state is PinValidationUiState.Validating
    val isError = state is PinValidationUiState.Error
    val keyboardEnabled = !isBlocked && !isValidating

    val filledCount = when (state) {
        is PinValidationUiState.Entering -> state.digits.size
        else -> 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HandleBar()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Shield,
                contentDescription = stringResource(R.string.pin_shield_description),
                tint = MenbresiaColors.Primary,
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = stringResource(R.string.pin_title),
                color = MenbresiaColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Text(
                text = stringResource(R.string.pin_subtitle)+"\n${venue?.name}",
                color = MenbresiaColors.TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            BenefitBadge(benefit = venue?.benefits?.get(CURRENT_BENEFIT).orEmpty())
        }

        PinIndicatorRow(
            filledCount = filledCount,
            isError = isError
        )

        when {
            isBlocked -> BlockedOverlay(state as PinValidationUiState.Blocked)
            isValidating -> CircularProgressIndicator(
                color = MenbresiaColors.Primary,
                modifier = Modifier.size(32.dp)
            )
            isError -> {
                Text(
                    text = stringResource(
                        R.string.pin_attempts_remaining,
                        (state as PinValidationUiState.Error).attemptsLeft
                    ),
                    color = MenbresiaColors.Error,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
            else -> Box(modifier = Modifier.height(20.dp))
        }

        PinKeyboard(
            onDigitPressed = { onIntent(PinValidationIntent.DigitPressed(it)) },
            onBackspacePressed = { onIntent(PinValidationIntent.BackspacePressed) },
            enabled = keyboardEnabled,
            modifier = Modifier.weight(1f, fill = false)
        )

        Text(
            text = stringResource(R.string.pin_cancel),
            color = MenbresiaColors.TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clickable { onIntent(PinValidationIntent.CancelPressed) }
        )
    }
}

